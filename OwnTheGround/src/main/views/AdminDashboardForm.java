package main.views;

import main.services.DatabaseService;
import main.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardForm extends JFrame {
    private final DatabaseService db = DatabaseService.getInstance();
    private JTable userTable;
    private DefaultTableModel userTableModel;

    public AdminDashboardForm() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table columns
        String[] columnNames = {"Username", "Email", "Admin?"};

        // Get user data
        List<User> users = db.getAllUsers();
        String[][] data = new String[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getEmail();
            data[i][2] = user.isAdmin() ? "Yes" : "No";
        }

        userTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton viewUserBtn = new JButton("View User");
        buttonPanel.add(viewUserBtn);

        viewUserBtn.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) userTable.getValueAt(selectedRow, 0);
                String email = (String) userTable.getValueAt(selectedRow, 1);
                String isAdmin = (String) userTable.getValueAt(selectedRow, 2);
                JOptionPane.showMessageDialog(this,
                    "Username: " + username + "\nEmail: " + email + "\nAdmin: " + isAdmin);
            } else {
                JOptionPane.showMessageDialog(this, "Select a user first.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton deleteUserBtn = new JButton("Delete User");
        buttonPanel.add(deleteUserBtn);

        deleteUserBtn.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) userTable.getValueAt(selectedRow, 0);
                if (username.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Cannot delete the default admin user.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user: " + username + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = db.deleteUser(username);
                    if (deleted) {
                        userTableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user first.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton toggleAdminBtn = new JButton("Toggle Admin");
        buttonPanel.add(toggleAdminBtn);
        toggleAdminBtn.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) userTable.getValueAt(selectedRow, 0);
                if (username.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Cannot change admin status of the default admin user.", "Modification Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                User user = db.getUser(username);
                if (user != null) {
                    boolean newStatus = !user.isAdmin();
                    user.setAdmin(newStatus);
                    db.updateUser(user);
                    userTable.setValueAt(newStatus ? "Yes" : "No", selectedRow, 2);
                    JOptionPane.showMessageDialog(this,
                        "User " + username + " is now " + (newStatus ? "an admin." : "a regular user."));
                } else {
                    JOptionPane.showMessageDialog(this, "User not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user first.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton manageProductsBtn = new JButton("Manage Products");
        manageProductsBtn.addActionListener(e -> {
            ManageProductsForm manageProductsForm = new ManageProductsForm();
            manageProductsForm.setVisible(true);
        });
        buttonPanel.add(manageProductsBtn);

        JButton viewOrdersBtn = new JButton("View Orders");
        viewOrdersBtn.addActionListener(e -> {
            ViewOrdersForm viewOrdersForm = new ViewOrdersForm();
            viewOrdersForm.setVisible(true);
        });
        buttonPanel.add(viewOrdersBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
    }
}