package bank.transfer;

import bank.model.Account;

public class OrderedTransfer {

    public static void transfer(Account from, Account to, double amount) {

        Account first;
        Account second;

        // Always lock the account with the smaller ID first
        if (from.getId() < to.getId()) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        System.out.println(
                Thread.currentThread().getName()
                        + " transferring ₹"
                        + amount
                        + " from "
                        + from.getName()
                        + " to "
                        + to.getName()
        );

        first.getLock().lock();

        try {

            System.out.println(
                    Thread.currentThread().getName()
                            + " locked "
                            + first.getName()
            );

            second.getLock().lock();

            try {

                System.out.println(
                        Thread.currentThread().getName()
                                + " locked "
                                + second.getName()
                );

                // Perform the actual transfer
                from.withdraw(amount);
                to.deposit(amount);

                System.out.println(
                        Thread.currentThread().getName()
                                + " completed transfer"
                );

            } finally {

                second.getLock().unlock();

                System.out.println(
                        Thread.currentThread().getName()
                                + " unlocked "
                                + second.getName()
                );
            }

        } finally {

            first.getLock().unlock();

            System.out.println(
                    Thread.currentThread().getName()
                            + " unlocked "
                            + first.getName()
            );
        }
    }
}