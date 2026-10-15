package bank.transfer;

import bank.model.Account;

public class DeadlockTransfer {

    public static void transfer(Account from, Account to, double amount) {

        System.out.println(
                Thread.currentThread().getName()
                        + " trying to transfer ₹"
                        + amount
                        + " from "
                        + from.getName()
                        + " to "
                        + to.getName()
        );

        // Lock the first account
        from.getLock().lock();

        try {

            System.out.println(
                    Thread.currentThread().getName()
                            + " locked "
                            + from.getName()
            );

            // Small delay so the other thread gets a chance
            // to acquire the other lock.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(
                    Thread.currentThread().getName()
                            + " waiting for "
                            + to.getName()
            );

            // Try to acquire the second account's lock
            to.getLock().lock();

            try {

                System.out.println(
                        Thread.currentThread().getName()
                                + " locked "
                                + to.getName()
                );

                // Perform transfer
                from.withdraw(amount);
                to.deposit(amount);

                System.out.println(
                        Thread.currentThread().getName()
                                + " completed transfer"
                );

            } finally {

                to.getLock().unlock();

            }

        } finally {

            from.getLock().unlock();

        }
    }
}