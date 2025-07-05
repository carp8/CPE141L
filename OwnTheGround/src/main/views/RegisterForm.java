package main.views;

import main.controllers.AuthController;
import main.models.User;
import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JTextField emailField = new JTextField(15);
    private final JTextArea addressArea = new JTextArea(3, 15);
    private final AuthController authController = new AuthController();

    public RegisterForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("OwnTheGround - Register");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields
        addField(panel, gbc, "Username:", usernameField, 0);
        addField(panel, gbc, "Password:", passwordField, 1);
        addField(panel, gbc, "Email:", emailField, 2);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        getContentPane().add(panel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void register() {
        User user = new User(
            usernameField.getText(),
            new String(passwordField.getPassword()),
            emailField.getText(),
            addressArea.getText(),
            false
        );

        if (authController.register(user)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
