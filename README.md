# 🏦 Bank Deadlock Demo

A Java multithreading project that demonstrates how **deadlocks occur during concurrent bank transfers** and how to handle them using lock ordering and timed lock acquisition.

The project simulates a small bank with multiple accounts and concurrent transactions. It intentionally reproduces a deadlock, then explores techniques to prevent indefinite waiting and maintain thread safety.

## 🎯 Project Objectives

- Understand Java multithreading and concurrent execution.
- Reproduce a real deadlock using multiple threads and locks.
- Prevent deadlocks through consistent lock ordering.
- Use `ReentrantLock` and `tryLock()` with timeouts.
- Understand lock acquisition, release, and `finally` blocks.
- Verify that concurrent transfers preserve the bank's total balance.

## 🛠️ Tech Stack

- **Language:** Java
- **Concurrency:** `Thread`, `ReentrantLock`, `tryLock()`
- **IDE:** IntelliJ IDEA
- **Build:** Standard Java project (no external dependencies required)

## 📂 Project Structure

```text
Bank-Deadlock-Demo/
├── src/
│   └── main/
│       └── java/
│           └── bank/
│               ├── Main.java
│               ├── TimeoutDemo.java
│               ├── model/
│               │   └── Account.java
│               ├── service/
│               │   └── Bank.java
│               └── transfer/
│                   ├── DeadlockTransfer.java
│                   ├── OrderedTransfer.java
│                   └── TimeoutTransfer.java
├── .gitignore
└── README.md
```

## ⚙️ Implementations

### 1. DeadlockTransfer — The Problem

Simulates two simultaneous transactions:

- Thread 1 transfers money from Alice to Bob.
- Thread 2 transfers money from Bob to Alice.

Each thread locks its source account before attempting to lock the destination account. This can produce a circular wait in which neither thread can proceed.

**Expected behavior:** The application may freeze because both threads are waiting for locks held by each other.

### 2. OrderedTransfer — Deadlock Prevention

Prevents circular wait by acquiring account locks in a consistent order based on account IDs, regardless of the direction of the transfer.

For example:

```text
Alice (ID 1) → Bob (ID 2)

Lock order: Alice → Bob

Bob (ID 2) → Alice (ID 1)

Lock order: Alice → Bob
```

**Expected behavior:** Both transfers complete without deadlocking.

### 3. TimeoutTransfer — Timed Lock Acquisition

Uses `ReentrantLock.tryLock()` with a timeout to avoid waiting indefinitely for a lock.

If a lock cannot be acquired within the configured time, the transfer is abandoned and returns `false`. Successfully acquired locks are released using `finally` blocks.

This implementation also uses consistent lock ordering.

**Expected behavior:** Transfers complete when locks are available; otherwise, the method can return `false` when acquisition times out.

### 4. TimeoutDemo — Demonstrating Timeout Behavior

Creates controlled lock contention between two threads. One thread holds Bob's lock while another attempts to acquire it with a timeout.

**Expected behavior:** The waiting thread prints a timeout message and releases its acquired lock if Bob's lock remains unavailable beyond the timeout.

## ▶️ How to Run

### Prerequisites

- JDK 17 or later
- IntelliJ IDEA or another Java IDE

### Run the demos

1. Clone or download this repository.
2. Open the project in your IDE.
3. Ensure `src/main/java` is marked as the Sources Root.
4. Open `Main.java` and run it to test the selected transfer implementation.
5. Run `TimeoutDemo.java` separately to observe timed lock acquisition.

To compare implementations, change the transfer class used by `Main.java` between `DeadlockTransfer`, `OrderedTransfer`, and `TimeoutTransfer`.

**Note:** `DeadlockTransfer` is intentionally unsafe and can cause the application to hang. Stop the process manually if it deadlocks. Run the other implementations separately.

## 💰 Balance Consistency

The bank starts with these accounts:

| Account | Initial Balance |
|---|---:|
| Alice | ₹5,000 |
| Bob | ₹3,000 |
| Carol | ₹2,000 |
| **Total** | **₹10,000** |

Money transfers should change individual account balances without changing the total amount of money in the bank.

```text
Initial total = ₹10,000
Final total   = ₹10,000
```

The total-balance check is a useful consistency check. For concurrent testing, the balance calculation should also be performed safely so it does not read accounts halfway through a transfer.

## 🧠 Key Concepts Learned

- Multithreading and thread scheduling
- Critical sections and mutual exclusion
- Race conditions and thread safety
- Deadlock and circular wait
- `ReentrantLock`
- Lock ordering
- `tryLock()` and timeout-based acquisition
- `try-finally` lock-release patterns
- Concurrent transaction consistency

## 🚀 Future Improvements

- Stress-test with hundreds or thousands of concurrent transfers.
- Add automatic retries for transfers that time out.
- Validate account balances and reject invalid withdrawals.
- Replace floating-point balances with integer minor units or `BigDecimal`.
- Add automated tests for transfer correctness and deadlock prevention.
- Add safe balance snapshots and transaction statistics.
- Measure throughput and compare the different strategies.

## 👨‍💻 Author

Developed as a hands-on Java concurrency project to explore deadlocks, lock management, and thread-safe bank transactions.
