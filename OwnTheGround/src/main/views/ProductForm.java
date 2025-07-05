package main.views;

import main.models.Products;
import main.services.DatabaseService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductForm extends JFrame {
    private final DatabaseService db = DatabaseService.getInstance();

    public ProductForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("OwnTheGround - Products");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel productPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Products> products = db.getProducts();
        for (Products product : products) {
            productPanel.add(createProductCard(product));
        }

        JScrollPane scrollPane = new JScrollPane(productPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        mainPanel.add(backButton, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private JPanel createProductCard(Products product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Image placeholder
        JLabel imageLabel = new JLabel(new ImageIcon(
            new ImageIcon(getClass().getResource("/placeholder.jpg"))
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        card.add(imageLabel, BorderLayout.CENTER);

        // Products info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(new JLabel(product.getName()));
        infoPanel.add(new JLabel(String.format("₱%,.2f", product.getPrice())));
        
        // Size selection
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.add(new JLabel("Size:"));
        ButtonGroup sizeGroup = new ButtonGroup();
        for (String size : product.getSizes()) {
            JRadioButton sizeBtn = new JRadioButton(size);
            sizeGroup.add(sizeBtn);
            sizePanel.add(sizeBtn);
        }
        infoPanel.add(sizePanel);

        card.add(infoPanel, BorderLayout.SOUTH);
        return card;
    }
}
