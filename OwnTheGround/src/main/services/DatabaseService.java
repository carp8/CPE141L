package main.services;

import main.models.*;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class DatabaseService {
    private static DatabaseService instance;
    private final Map<String, User> users = new HashMap<>();
    private final List<Products> products = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    private final String dataDir = "data";
    private final String usersFile = dataDir + "/users.dat";
    private final String productsFile = dataDir + "/products.dat";
    private final String ordersFile = dataDir + "/orders.dat";
    private final String transactionsFile = dataDir + "/transactions.dat";

    private DatabaseService() {
        File dir = new File(dataDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        loadData();

        if(users.isEmpty()) {
            users.put("admin", new User("admin", "admin123", "admin@example.com", "123 Admin St", true));
            saveData();
        }
        if(products.isEmpty()) {
            initProducts();
        }
    }

    public static DatabaseService getInstance() {
        if(instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void initProducts() {
        products.add(new Products(1, "Muscon", "Italian leather", 2199.00, new String[]{"38", "39", "40", "41", "42"}));
        products.add(new Products(2, "Noir", "Italian leather", 2199.00, new String[]{"38", "39", "40", "41", "42"}));
        products.add(new Products(3, "Charcolux", "Italian leather", 2199.00, new String[]{"36", "37", "38", "39", "40"}));
        products.add(new Products(4, "Gentleman", "Italian leather", 2199.00, new String[]{"40", "41", "42", "43", "44"}));
        saveData();
    }


    public boolean addUser(User user) {
        if(users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        saveData();
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void updateUser(User user) {
        users.put(user.getUsername(), user);
        saveData();
    }

    public boolean deleteUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            saveData();
            return true;
        }
        return false;
    }

    public List<Products> getProducts() {
        return new ArrayList<>(products);
    }

    public Products getProductById(int id) {
        return products.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst()
                       .orElse(null);
    }

    public boolean addProduct(Products product) {
        if (getProductById(product.getId()) != null) {
            return false;
        }
        products.add(product);
        saveData();
        return true;
    }

    public boolean updateProduct(Products updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        boolean removed = products.removeIf(p -> p.getId() == productId);
        if (removed) {
            saveData();
        }
        return removed;
    }
 
    public void addOrder(Order order) {
        orders.add(order);
        saveData();
    }

    public List<Order> getOrders(String username) {
        List<Order> userOrders = new ArrayList<>();
        for(Order o : orders) {
            if(o.getUsername().equals(username)) {
                userOrders.add(o);
            }
        }
        return userOrders;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order getOrderById(String orderId) {
        return orders.stream()
                     .filter(o -> o.getOrderId().equals(orderId))
                     .findFirst()
                     .orElse(null);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveData();
    }

    public List<Transaction> getUserTransactions(String username) {
        List<Transaction> userTransactions = new ArrayList<>();
        for(Transaction t : transactions) {
            if(t.getUsername().equals(username)) {
                userTransactions.add(t);
            }
        }
        return userTransactions;
    }

    private void loadData() {
        try {
            File dir = new File(dataDir);
            if(!dir.exists()) dir.mkdirs();

            if(new File(usersFile).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile))) {
                    Map<String, User> loadedUsers = (Map<String, User>) ois.readObject();
                    users.putAll(loadedUsers);
                }
            }

            if(new File(productsFile).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(productsFile))) {
                    List<Products> loadedProducts = (List<Products>) ois.readObject();
                    products.addAll(loadedProducts);
                }
            }

            if(new File(ordersFile).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ordersFile))) {
                    List<Order> loadedOrders = (List<Order>) ois.readObject();
                    orders.addAll(loadedOrders);
                }
            }

            if(new File(transactionsFile).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(transactionsFile))) {
                    List<Transaction> loadedTransactions = (List<Transaction>) ois.readObject();
                    transactions.addAll(loadedTransactions);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Data files not found, starting with empty data (this is normal for first run or if files were deleted).");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        try {
            File dir = new File(dataDir);
            if(!dir.exists()) dir.mkdirs();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile))) {
                oos.writeObject(users);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(productsFile))) {
                oos.writeObject(products);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ordersFile))) {
                oos.writeObject(orders);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(transactionsFile))) {
                oos.writeObject(transactions);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}