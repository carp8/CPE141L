package main.views;

import main.models.Products;
import main.services.DatabaseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList; // Used to collect valid sizes
import java.util.List;

public class ManageProductsForm extends JFrame {
    private final DatabaseService db = DatabaseService.getInstance();
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ManageProductsForm() {
        setTitle("Manage Products");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadProductData();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Description", "Price", "Sizes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addProductBtn = new JButton("Add Product");
        JButton editProductBtn = new JButton("Edit Product");
        JButton deleteProductBtn = new JButton("Delete Product");

        buttonPanel.add(addProductBtn);
        buttonPanel.add(editProductBtn);
        buttonPanel.add(deleteProductBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(panel);

        addProductBtn.addActionListener(e -> addProduct());
        editProductBtn.addActionListener(e -> editProduct());
        deleteProductBtn.addActionListener(e -> deleteProduct());
    }

    private void loadProductData() {
        tableModel.setRowCount(0);
        List<Products> products = db.getProducts();
        for (Products product : products) {
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    String.format("%.2f", product.getPrice()),
                    String.join(", ", product.getSizes())
            });
        }
    }

    private void addProduct() {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JTextField sizesField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Sizes (comma-separated):"));
        inputPanel.add(sizesField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Add New Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate Product ID
                int id = Integer.parseInt(idField.getText().trim());
                if (id < 0) {
                    JOptionPane.showMessageDialog(this, "Product ID cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();

                // Validate Price
                double price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sizesInput = sizesField.getText().trim();
                String[] rawSizes = sizesInput.split("\\s*,\\s*");
                List<String> validSizesList = new ArrayList<>(); // To collect only valid numerical sizes

                if (name.isEmpty() || description.isEmpty() || sizesInput.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields (Name, Description, Sizes).", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // --- Validate Sizes: No negative, and only valid numbers ---
                for (String sizeStr : rawSizes) {
                    String trimmedSizeStr = sizeStr.trim();
                    if (trimmedSizeStr.isEmpty()) {
                        continue; // Skip empty strings from multiple commas (e.g., "38,,40")
                    }

                    try {
                        int sizeNum = Integer.parseInt(trimmedSizeStr); // Attempt to parse as an integer
                        if (sizeNum < 0) {
                            JOptionPane.showMessageDialog(this, "Sizes cannot be negative. Invalid size: " + trimmedSizeStr, "Input Error", JOptionPane.ERROR_MESSAGE);
                            return; // Stop processing if any negative numerical size is found
                        }
                        validSizesList.add(trimmedSizeStr); // ONLY add to list if it's a valid non-negative number
                    } catch (NumberFormatException e) {
                        // This block is entered if sizeStr is NOT a valid integer (e.g., "XL", "M", "abc")
                        JOptionPane.showMessageDialog(this, "Invalid size format. Sizes must be positive numbers only. Invalid size: " + trimmedSizeStr, "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop processing immediately if a non-numeric character is found
                    }
                }

                if (validSizesList.isEmpty()) { // Check if any valid sizes were collected
                    JOptionPane.showMessageDialog(this, "Please enter at least one valid numerical size.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] finalSizes = validSizesList.toArray(new String[0]); // Convert the list of valid sizes back to an array

                boolean idExists = db.getProducts().stream().anyMatch(p -> p.getId() == id);
                if (idExists) {
                    JOptionPane.showMessageDialog(this, "Product with this ID already exists. Please use a unique ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Products newProduct = new Products(id, name, description, price, finalSizes); // Use finalSizes
                if (db.addProduct(newProduct)) {
                    loadProductData();
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add product. An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                // This catch now primarily handles errors from Product ID or Price parsing
                JOptionPane.showMessageDialog(this, "Invalid Product ID or Price. Please enter valid numbers for these fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String description = (String) tableModel.getValueAt(selectedRow, 2);
        double price = Double.parseDouble((String) tableModel.getValueAt(selectedRow, 3));
        String sizesString = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField idField = new JTextField(String.valueOf(id), 5);
        idField.setEditable(false);
        JTextField nameField = new JTextField(name, 20);
        JTextField descriptionField = new JTextField(description, 20);
        JTextField priceField = new JTextField(String.valueOf(price), 10);
        JTextField sizesField = new JTextField(sizesString, 20);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        // Changed label for clarity regarding numeric-only sizes
        inputPanel.add(new JLabel("Sizes (comma-separated):"));
        inputPanel.add(sizesField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Edit Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                String newDescription = descriptionField.getText().trim();

                // Validate Price
                double newPrice = Double.parseDouble(priceField.getText().trim());
                if (newPrice < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String newSizesInput = sizesField.getText().trim();
                String[] rawNewSizes = newSizesInput.split("\\s*,\\s*");
                List<String> validNewSizesList = new ArrayList<>(); // To collect only valid numerical sizes

                if (newName.isEmpty() || newDescription.isEmpty() || newSizesInput.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields (Name, Description, Sizes).", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // --- Validate Sizes: No negative, and only valid numbers ---
                for (String sizeStr : rawNewSizes) {
                    String trimmedSizeStr = sizeStr.trim();
                    if (trimmedSizeStr.isEmpty()) {
                        continue; // Skip empty strings from multiple commas
                    }

                    try {
                        int sizeNum = Integer.parseInt(trimmedSizeStr); // Attempt to parse as an integer
                        if (sizeNum < 0) {
                            JOptionPane.showMessageDialog(this, "Sizes cannot be negative. Invalid size: " + trimmedSizeStr, "Input Error", JOptionPane.ERROR_MESSAGE);
                            return; // Stop processing if any negative numerical size is found
                        }
                        validNewSizesList.add(trimmedSizeStr); // ONLY add to list if it's a valid non-negative number
                    } catch (NumberFormatException e) {
                        // This block is entered if sizeStr is NOT a valid integer (e.g., "XL", "M", "abc")
                        JOptionPane.showMessageDialog(this, "Invalid size. Sizes must be numbers only. Invalid size: " + trimmedSizeStr, "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop processing immediately if a non-numeric character is found
                    }
                }

                if (validNewSizesList.isEmpty()) { // Check if any valid sizes were collected
                    JOptionPane.showMessageDialog(this, "Please enter at least one valid numerical size.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] finalNewSizes = validNewSizesList.toArray(new String[0]); // Convert the list of valid sizes back to an array

                Products updatedProduct = new Products(id, newName, newDescription, newPrice, finalNewSizes); // Use finalNewSizes
                if (db.updateProduct(updatedProduct)) {
                    loadProductData();
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update product. An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                // This catch now primarily handles errors from Price parsing
                JOptionPane.showMessageDialog(this, "Invalid Price. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete product: " + productName + " (ID: " + productId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deleteProduct(productId)) {
                loadProductData();
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
