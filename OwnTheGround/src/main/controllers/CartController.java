package main.controllers;

import main.models.CartItem;
import main.models.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartController {
    private final List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Products product, String size, int quantity) {
        Optional<CartItem> existing = cartItems.stream()
            .filter(i -> i.getProduct().getId() == product.getId() && i.getSize().equals(size))
            .findFirst();

        if(existing.isPresent()) {
            int newQty = existing.get().getQuantity() + quantity;
            existing.get().setQuantity(newQty);
        } else {
            cartItems.add(new CartItem(product, size, quantity));
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }
}
