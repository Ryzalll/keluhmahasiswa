package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Patient {
    public String id;
    public String nama;
    public String nim;
    public String alamat;
    public String keluhan;
    public String solusi;
    public String tanggal;
    public String tekananDarah;

    public Patient() {}

    public Patient(String id, String nama, String nim, String alamat, String keluhan) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.alamat = alamat;
        this.keluhan = keluhan;
        this.solusi = "";
        this.tekananDarah = "";

        // Tanggal otomatis saat pendaftaran
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        this.tanggal = LocalDate.now().format(formatter);
    }
}
