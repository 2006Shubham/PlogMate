package com.example.megamart.managefunds.expense;

public class Expense {
    private int eventId;
    private String expenseName;
    private double amount;

    public Expense(int eventId, String expenseName, double amount) {
        this.eventId = eventId;
        this.expenseName = expenseName;
        this.amount = amount;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
