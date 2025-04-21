package models;

public class User {
    public String username;
    public String password;
    public String role; // contoh: "dokter" atau "admin"

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
