package main.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private final Products product;
    private final String size;
    private int quantity;

    public CartItem(Products product, String size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    public Products getProduct() { return product; }
    public String getSize() { return size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
