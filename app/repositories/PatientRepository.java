package models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    private static final File FILE = new File("data/patients.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Patient> getAllPatients() {
        try {
            if (!FILE.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(FILE, new TypeReference<List<Patient>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAllPatients(List<Patient> patients) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, patients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
