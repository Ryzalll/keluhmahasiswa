package controllers;

import play.mvc.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class AuthController extends Controller {

    // User hardcoded default (dosen dan admin)
    private final List<User> defaultUsers = Arrays.asList(
        new User("dosen", "123", "dosen"),
        new User("admin", "123", "admin")
    );

    // Halaman login
    public Result loginPage() {
        return ok(views.html.login.render(""));
    }

    // Proses login
    public Result login(Http.Request request) {
        Map<String, String[]> form = request.body().asFormUrlEncoded();
        String username = form.get("username")[0];
        String password = form.get("password")[0];

        List<User> users = new ArrayList<>(UserRepository.getUsers());
        users.addAll(defaultUsers); // ⬅️ penting ini

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                if (user.role.equals("dosen")) {
                    return redirect(routes.PatientController.history())
                        .addingToSession(request, Map.of("user", username, "role", user.role));
                } else {
                    return redirect(routes.HomeController.index())
                        .addingToSession(request, Map.of("user", username, "role", user.role));
                }
            }
        }

        return unauthorized("Login gagal");
    }



    // Logout
    public Result logout(Http.Request request) {
        return redirect(routes.AuthController.loginPage()).withNewSession();
    }

    // Halaman form register akun
    public Result registerPage() {
        return ok(views.html.registerAccount.render("Registrasi Akun"));
    }

    public Result registerAccount(Http.Request request) {
        Map<String, String[]> form = request.body().asFormUrlEncoded();
        String username = form.get("username")[0];
        String password = form.get("password")[0];

        List<User> users = UserRepository.getUsers();
        users.add(new User(username, password, "pasien"));
        UserRepository.saveUsers(users);

        // Login otomatis setelah registrasi
        return redirect(routes.HomeController.index())
            .addingToSession(request, Map.of("user", username, "role", "pasien"));
    }

}
