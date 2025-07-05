package main.models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final String transactionId;
    private final String username;
    private final Date transactionDate;
    private final double amount;
    private final String status;

    public Transaction(String username, Date transactionDate, double amount, String status) {
        this.transactionId = "TXN-" + System.currentTimeMillis();
        this.username = username;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public String getUsername() { return username; }
    public Date getTransactionDate() { return transactionDate; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
}
