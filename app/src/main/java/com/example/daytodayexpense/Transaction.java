package com.example.daytodayexpense;

public class Transaction {
    private String amount;
    private String description;

    public Transaction(String amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public Transaction(String amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public Transaction() {
        // Default constructor required for Firebase
    }


    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
