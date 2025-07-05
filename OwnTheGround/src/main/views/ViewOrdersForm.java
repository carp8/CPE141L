package main.views;

import main.models.Order;
import main.models.CartItem;
import main.services.DatabaseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewOrdersForm extends JFrame {
    private final DatabaseService db = DatabaseService.getInstance();
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public ViewOrdersForm() {
        setTitle("View All Orders");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadOrderData();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Order ID", "Username", "Order Date", "Total Price", "Delivery Option", "Delivery Address", "Payment Method", "Items"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(80); 
        orderTable.getColumnModel().getColumn(7).setPreferredWidth(250);


        JScrollPane scrollPane = new JScrollPane(orderTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton viewDetailsBtn = new JButton("View Order Details");
        buttonPanel.add(viewDetailsBtn);

        viewDetailsBtn.addActionListener(e -> viewOrderDetails());

        panel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(panel);
    }
    
    private void loadOrderData() {
        tableModel.setRowCount(0);
        List<Order> allOrders = db.getAllOrders();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Order order : allOrders) {
            StringBuilder itemsDisplay = new StringBuilder();
            for (CartItem item : order.getItems()) {
                itemsDisplay.append(item.getProduct().getName()) 
                            .append(" (Size: ")
                            .append(item.getSize()) 
                            .append(", Qty: ")
                            .append(item.getQuantity())
                            .append("), ");
            }
            String finalItemsString = itemsDisplay.length() > 0 ? itemsDisplay.substring(0, itemsDisplay.length() - 2) : "";

            tableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getUsername(),
                    sdf.format(order.getOrderDate()),
                    String.format("%.2f", order.getTotalPrice()),
                    order.getDeliveryOption(),
                    order.getDeliveryAddress(),
                    order.getPaymentMethod(),
                    finalItemsString
            });
        }
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);
        Order selectedOrder = db.getOrderById(orderId);

        if (selectedOrder != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder details = new StringBuilder();
            details.append("Order ID: ").append(selectedOrder.getOrderId()).append("\n");
            details.append("Customer: ").append(selectedOrder.getUsername()).append("\n");
            details.append("Order Date: ").append(sdf.format(selectedOrder.getOrderDate())).append("\n");
            details.append("Total Price: ").append(String.format("₱%.2f", selectedOrder.getTotalPrice())).append("\n");
            details.append("Delivery Option: ").append(selectedOrder.getDeliveryOption()).append("\n");
            details.append("Delivery Address: ").append(selectedOrder.getDeliveryAddress()).append("\n");
            details.append("Payment Method: ").append(selectedOrder.getPaymentMethod()).append("\n\n");
            details.append("--- Ordered Items ---\n");
            for (CartItem item : selectedOrder.getItems()) {
                details.append("- ").append(item.getProduct().getName()) 
                       .append(" (Size: ").append(item.getSize()) 
                       .append(", Quantity: ").append(item.getQuantity())
                       .append(", Price: ").append(String.format("₱%.2f", item.getProduct().getPrice() * item.getQuantity())) 
                       .append(")\n");
            }

            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Order Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Order not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}