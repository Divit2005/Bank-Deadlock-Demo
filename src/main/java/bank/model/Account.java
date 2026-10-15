package bank.model;

import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final int id;
    private final String name;
    private double balance;

    private final ReentrantLock lock = new ReentrantLock();

    public Account(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    @Override
    public String toString() {
        return name + " [" + id + "] : ₹" + balance;
    }
}