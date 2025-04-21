package repositories;

import models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_PATH = "data/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<User> getUsers() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // Buat folder jika belum ada
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
