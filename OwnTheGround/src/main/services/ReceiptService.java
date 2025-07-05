package main.services;

import main.models.Order;
import main.models.CartItem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class ReceiptService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public String generateReceipt(Order order) {
        String fileName = "receipts/receipt_" + dateFormat.format(order.getOrderDate()) + ".txt";
        try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("======= OWN THE GROUND RECEIPT ========");
            writer.println("Order ID: " + order.getOrderId());
            writer.println("Customer: " + order.getUsername());
            writer.println("Date: " + order.getOrderDate());
            writer.println("---------------------------------------");
            writer.println("Qty  Product           Size    Price    Subtotal");
            for(CartItem item : order.getItems()) {
                writer.printf("%-4d %-16s %-6s ₱%-7.2f ₱%.2f\n",
                        item.getQuantity(),
                        item.getProduct().getName(),
                        item.getSize(),
                        item.getProduct().getPrice(),
                        item.getProduct().getPrice() * item.getQuantity()
                );
            }
            writer.println("---------------------------------------");
            double deliveryFee = order.getDeliveryOption().equalsIgnoreCase("Delivery") ? 100.00 : 0.00;
            writer.printf("Subtotal:     ₱%.2f\n", order.getTotalPrice());
            writer.printf("Delivery Fee: ₱%.2f\n", deliveryFee);
            writer.printf("Total:        ₱%.2f\n", order.getTotalPrice() + deliveryFee);
            writer.println("---------------------------------------");
            writer.println("Delivery Option: " + order.getDeliveryOption());
            if(order.getDeliveryOption().equalsIgnoreCase("Delivery")) {
                writer.println("Delivery Address: " + order.getDeliveryAddress());
            }
            writer.println("Payment Method: " + order.getPaymentMethod());
            writer.println("=======================================");
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
