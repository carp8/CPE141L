package main.views;

import javax.swing.*;
import java.awt.*; // Import statement for Font
import java.time.LocalDateTime; // Import for date and time

public class ReceiptForm extends JFrame {
    public ReceiptForm(String username) {
        initComponents(username);
        setLocationRelativeTo(null);
    }

    private void initComponents(String username) {
        setTitle("OwnTheGround - Order Receipt");
        setSize(600, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create a JTextArea for displaying the receipt
        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Generate receipt content
        String receipt = generateReceiptContent(username);
        receiptArea.setText(receipt);

        // Add JTextArea to a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        getContentPane().add(scrollPane);
    }

    private String generateReceiptContent(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("          OWN THE GROUND\n");
        sb.append("        Premium Footwear Shop\n");
        sb.append("------------------------------------\n");
        sb.append("Customer: ").append(username).append("\n");
        sb.append("Date: ").append(LocalDateTime.now()).append("\n");
        sb.append("====================================\n");
        sb.append("Thank you for your purchase!\n");
        sb.append("Your order has been processed.\n");
        sb.append("====================================\n");
        return sb.toString();
    }
}
