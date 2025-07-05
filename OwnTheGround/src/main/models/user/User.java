package main.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String address;
    private boolean isAdmin;

    public User(String username, String password, String email, String address, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public boolean isAdmin() { return isAdmin; }
    
    public void setAdmin(boolean admin) {
    this.isAdmin = admin;
    }
}
