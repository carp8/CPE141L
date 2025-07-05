package main.views;

import javax.swing.*;
import java.awt.*;

public class CheckoutForm extends JFrame {
    public CheckoutForm(String username) {
        initComponents(username);
        setLocationRelativeTo(null);
    }

    private void initComponents(String username) {
        setTitle("OwnTheGround - Checkout");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Delivery options
        gbc.gridx = gbc.gridy = 0;
        mainPanel.add(new JLabel("Delivery Option:"), gbc);
        
        JRadioButton pickupBtn = new JRadioButton("Pick Up");
        JRadioButton deliveryBtn = new JRadioButton("Delivery");
        ButtonGroup deliveryGroup = new ButtonGroup();
        deliveryGroup.add(pickupBtn);
        deliveryGroup.add(deliveryBtn);
        
        gbc.gridx = 1;
        mainPanel.add(pickupBtn, gbc);
        gbc.gridx = 2;
        mainPanel.add(deliveryBtn, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Delivery Address:"), gbc);
        
        JTextArea addressArea = new JTextArea(3, 30);
        addressArea.setEnabled(false); // Would enable if delivery selected
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(new JScrollPane(addressArea), gbc);

        // Payment
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Payment Method:"), gbc);
        
        String[] payments = {"Cash on Delivery", "Credit Card", "GCash"};
        JComboBox<String> paymentCombo = new JComboBox<>(payments);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(paymentCombo, gbc);

        // Confirm button
        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.addActionListener(e -> {
            new ReceiptForm(username).setVisible(true);
            dispose();
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(confirmButton, gbc);

        getContentPane().add(mainPanel);
    }
}
