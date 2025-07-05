package main.controllers;

import main.models.User;
import main.services.DatabaseService;

public class AuthController {
    private final DatabaseService db = DatabaseService.getInstance();
    private User currentUser;

    public boolean login(String username, String password) {
        User user = db.getUser(username);
        if(user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(User user) {
        return db.addUser(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
