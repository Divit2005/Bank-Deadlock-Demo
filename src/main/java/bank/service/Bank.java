package bank.service;

import bank.model.Account;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<Integer, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account getAccount(int id) {
        return accounts.get(id);
    }

    public double getTotalBalance() {
        double total = 0;

        for (Account account : accounts.values()) {
            total += account.getBalance();
        }

        return total;
    }

    public void printAccounts() {
        for (Account account : accounts.values()) {
            System.out.println(account);
        }
    }
}