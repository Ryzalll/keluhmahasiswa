package controllers;

import play.mvc.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import models.Patient;
import models.PatientRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class PatientController extends Controller {

    public Result registerPage() {
        return ok(views.html.register.render("Form Pendaftaran Pasien"));
    }

    public Result register(Http.Request request) {
        Map<String, String[]> form = request.body().asFormUrlEncoded();

        if (form == null || form.get("nama") == null || form.get("nim") == null ||
            form.get("alamat") == null || form.get("keluhan") == null) {
            return badRequest("Data tidak lengkap");
        }

        String nama = form.get("nama")[0];
        String nim = form.get("nim")[0];
        String alamat = form.get("alamat")[0];
        String keluhan = form.get("keluhan")[0];

        String id = UUID.randomUUID().toString().substring(0, 8);

        // Format tanggal lokal Indonesia
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("id", "ID"));
        String tanggalFormatted = LocalDate.now().format(formatter);

        Patient newPatient = new Patient(id, nama, nim, alamat, keluhan);
        newPatient.tanggal = tanggalFormatted;

        List<Patient> patients = PatientRepository.getAllPatients();
        patients.add(newPatient);
        PatientRepository.saveAllPatients(patients);

        return ok(views.html.registerSuccess.render(newPatient));
    }

    public Result history(Http.Request request) {
        String role = request.session().getOptional("role").orElse("");
        if (!role.equals("dosen")) {
            return unauthorized("Hanya dosen yang dapat melihat riwayat keluhan mahasiswa");
        }
    
        List<Patient> patients = PatientRepository.getAllPatients();
        return ok(views.html.history.render(patients, request));
    }
    

    public Result updatesolusi(Http.Request request) {
        Map<String, String[]> form = request.body().asFormUrlEncoded();
        String id = form.get("id")[0];
        String solusi = form.get("solusi")[0];
    
        List<Patient> patients = PatientRepository.getAllPatients();
    
        for (Patient patient : patients) {
            if (patient.id.equals(id)) {
                patient.solusi = solusi;
                break;
            }
        }
    
        PatientRepository.saveAllPatients(patients);
        return redirect(routes.PatientController.history());
    }

    public Result editPage(String id, Http.Request request) {
        List<Patient> patients = PatientRepository.getAllPatients();
        for (Patient patient : patients) {
            if (patient.id.equals(id)) {
                return ok(views.html.edit.render(patient, request));
            }
        }
        return notFound("Pasien tidak ditemukan");
    }


    public Result edit(Http.Request request) {
        Map<String, String[]> form = request.body().asFormUrlEncoded();
        String id = form.get("id")[0];
        String nama = form.get("nama")[0];
        String nim = form.get("nim")[0];
        String alamat = form.get("alamat")[0];
        String keluhan = form.get("keluhan")[0];
        String solusi = form.get("solusi")[0];

        String role = request.session().getOptional("role").orElse("");
        String tekananDarah = form.containsKey("tekananDarah") ? form.get("tekananDarah")[0] : "";

        List<Patient> patients = PatientRepository.getAllPatients();

        for (Patient patient : patients) {
            if (patient.id.equals(id)) {
                patient.nama = nama;
                patient.nim = nim;
                patient.alamat = alamat;
                patient.keluhan = keluhan;
                patient.solusi = solusi;
                if (role.equals("dokter")) {
                    patient.tekananDarah = tekananDarah;
                }
                break;
            }
        }

        PatientRepository.saveAllPatients(patients);
        return redirect(routes.PatientController.history());
    }



    
    public Result delete(String id) {
        List<Patient> patients = PatientRepository.getAllPatients();
        patients.removeIf(p -> p.id.equals(id));
        PatientRepository.saveAllPatients(patients);
        return redirect(routes.PatientController.history());
    }
    
    
}
