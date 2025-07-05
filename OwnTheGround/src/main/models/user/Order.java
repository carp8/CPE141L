package main.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private final String orderId;
    private final String username;
    private final List<CartItem> items;
    private final Date orderDate;
    private final String deliveryOption;
    private final String deliveryAddress;
    private final String paymentMethod;
    private final double totalPrice;

    public Order(String username, List<CartItem> items, Date orderDate,
                 String deliveryOption, String deliveryAddress, String paymentMethod, double totalPrice) {
        this.orderId = "ORD-" + System.currentTimeMillis();
        this.username = username;
        this.items = items;
        this.orderDate = orderDate;
        this.deliveryOption = deliveryOption;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public String getUsername() { return username; }
    public List<CartItem> getItems() { return items; }
    public Date getOrderDate() { return orderDate; }
    public String getDeliveryOption() { return deliveryOption; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalPrice() { return totalPrice; }
}
