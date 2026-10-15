package bank;

import bank.model.Account;
import bank.service.Bank;
import bank.transfer.DeadlockTransfer;
import bank.transfer.OrderedTransfer;
import bank.transfer.TimeoutTransfer;

public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();

        Account alice = new Account(1, "Alice", 5000);
        Account bob = new Account(2, "Bob", 3000);
        Account carol = new Account(3, "Carol", 2000);

        bank.addAccount(alice);
        bank.addAccount(bob);
        bank.addAccount(carol);

        System.out.println("Initial Accounts:");
        bank.printAccounts();

        System.out.println();
        System.out.println("Initial total: ₹" + bank.getTotalBalance());

        // Thread 1: Alice -> Bob
        Thread thread1 = new Thread(() -> {

            TimeoutTransfer.transfer(alice, bob, 1000);

        }, "Thread-1");

        // Thread 2: Bob -> Alice
        Thread thread2 = new Thread(() -> {

            TimeoutTransfer.transfer(bob, alice, 500);

        }, "Thread-2");

        System.out.println();
        System.out.println("Starting transfers...");

        thread1.start();
        thread2.start();

        // Wait for both threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
        System.out.println("Final Accounts:");
        bank.printAccounts();

        System.out.println();
        System.out.println("Final total: ₹" + bank.getTotalBalance());
    }
}