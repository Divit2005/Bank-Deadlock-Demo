package bank.transfer;

import bank.model.Account;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutTransfer {

    public static boolean transfer(Account from, Account to, double amount) {

        ReentrantLock firstLock;
        ReentrantLock secondLock;

        // We still choose an order for acquiring locks.
        if (from.getId() < to.getId()) {
            firstLock = from.getLock();
            secondLock = to.getLock();
        } else {
            firstLock = to.getLock();
            secondLock = from.getLock();
        }

        System.out.println(
                Thread.currentThread().getName()
                        + " trying to transfer ₹"
                        + amount
                        + " from "
                        + from.getName()
                        + " to "
                        + to.getName()
        );

        try {

            // Try to acquire the first lock
            if (!firstLock.tryLock(1, TimeUnit.SECONDS)) {

                System.out.println(
                        Thread.currentThread().getName()
                                + " could not acquire first lock"
                );

                return false;
            }

            try {

                System.out.println(
                        Thread.currentThread().getName()
                                + " locked first account"
                );

                // Try to acquire the second lock
                if (!secondLock.tryLock(1, TimeUnit.SECONDS)) {

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " could not acquire second lock"
                    );

                    return false;
                }

                try {

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " acquired both locks"
                    );

                    from.withdraw(amount);
                    to.deposit(amount);

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " completed transfer"
                    );

                    return true;

                } finally {

                    secondLock.unlock();

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " released second lock"
                    );
                }

            } finally {

                firstLock.unlock();

                System.out.println(
                        Thread.currentThread().getName()
                                + " released first lock"
                );
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    Thread.currentThread().getName()
                            + " was interrupted"
            );

            return false;
        }
    }
}