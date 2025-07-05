package main.views;

import main.models.User;
import main.controllers.AuthController;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private final AuthController authController;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        this.authController = new AuthController();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("OwnTheGround - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> new RegisterForm().setVisible(true));
        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        getContentPane().add(panel);
    }

    private void login() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    if (authController.login(username, password)) {
        User user = authController.getCurrentUser();
        if (user.isAdmin()) {
            // Show admin dashboard for admins
            new AdminDashboardForm().setVisible(true);
        } else {
            // Show main form for normal users
            new MainForm(user).setVisible(true);
        }
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
