package main.models;

import java.io.Serializable;

public class Products implements Serializable {
    private final int id;
    private final String name;
    private final String description;
    private final double price;
    private final String[] sizes;

    public Products(int id, String name, String description, double price, String[] sizes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sizes = sizes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String[] getSizes() { return sizes; }
}
