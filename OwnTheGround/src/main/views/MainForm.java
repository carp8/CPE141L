package main.views;

import main.models.User;
import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
    private final User currentUser;

    public MainForm(User currentUser) {
        this.currentUser = currentUser;
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("OwnTheGround - Welcome " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 107, 107));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        JLabel titleLabel = new JLabel("OWN THE GROUND");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Navigation
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] navItems = {"Products", "Cart", "History", "Account"};
        for (String item : navItems) {
            JButton button = new JButton(item);
            button.addActionListener(e -> handleNavButton(item));
            navPanel.add(button);
        }
        
        mainPanel.add(navPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
    }

    private void handleNavButton(String action) {
        switch (action) {
            case "Products":
                new ProductForm().setVisible(true);
                break;
            case "Cart":
                new CartForm(currentUser.getUsername()).setVisible(true);
                break;
            case "Account":
                showAccountInfo();
                break;
        }
    }

    private void showAccountInfo() {
        JOptionPane.showMessageDialog(this,
            "Username: " + currentUser.getUsername() + "\n" +
            "Email: " + currentUser.getEmail() + "\n" +
            "Address: " + currentUser.getAddress(),
            "Account Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
}