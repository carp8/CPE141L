package main.views;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class CartForm extends JFrame {
    public CartForm(String username) {
        initComponents(username);
        setLocationRelativeTo(null);
    }

    private void initComponents(String username) {
        setTitle("OwnTheGround - Shopping Cart");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Cart items table
        String[] columns = {"Product", "Size", "Quantity", "Price", "Subtotal"};
        Object[][] data = {}; // Would load from controller
        
        JTable cartTable = new JTable(data, columns);
        mainPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Checkout panel
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        
        JLabel totalLabel = new JLabel("Total: " + 
            NumberFormat.getCurrencyInstance().format(0)); // Would calculate from controller
        
        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.addActionListener(e -> new CheckoutForm(username).setVisible(true));
        
        checkoutPanel.add(totalLabel, BorderLayout.CENTER);
        checkoutPanel.add(checkoutButton, BorderLayout.EAST);
        
        mainPanel.add(checkoutPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
    }
}
