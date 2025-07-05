package main.controllers;

import main.models.*;
import main.services.DatabaseService;
import main.services.ReceiptService;

import java.util.Date;
import java.util.List;

public class OrderController {
    private final DatabaseService db = DatabaseService.getInstance();
    private final ReceiptService receiptService = new ReceiptService();
    private String currentUser;

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    public boolean processOrder(List<CartItem> cartItems,
                                String deliveryOption,
                                String deliveryAddress,
                                String paymentMethod) {
        if(cartItems.isEmpty()) return false;

        double totalPrice = cartItems.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();

        Order order = new Order(currentUser, cartItems, new Date(),
                deliveryOption, deliveryAddress, paymentMethod, totalPrice);

        Transaction transaction = new Transaction(currentUser, new Date(), totalPrice, "Completed");

        db.addOrder(order);
        db.addTransaction(transaction);

        String receiptFile = receiptService.generateReceipt(order);
        return receiptFile != null;
    }

    public List<Order> getUserOrders() {
        return db.getOrders(currentUser);
    }
}
