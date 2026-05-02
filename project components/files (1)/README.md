# 🏧 ATM Simulation System
Unit 6 Capstone Project — OOP with Java

Description:-
This Java desktop application simulates a real-world ATM system with
PIN-based authentication, cash withdrawal, deposit, fund transfer,
balance enquiry, and PIN management — built entirely using Java OOP
principles, Swing GUI, and a layered MVC architecture.

Team Members:-
Palak 590013843 |
Bhavya Shree 590014888 |
Ashvika Singh 590014516 |
Suzal Sahrawat 590015312 |
Girdhar Manchanda 590012105 |
Sajan Deol 590013805

---

## Project Structure

```
Project Components/
├── model/
│   ├── Account.java          # Account entity (Encapsulation)
│   └── Transaction.java      # Transaction entity (Enum, Immutable)
├── dao/
│   ├── AccountDAO.java       # Account data access (Collections, HashMap)
│   └── TransactionDAO.java   # Transaction data access (Generics)
├── service/
│   ├── ATMService.java       # Business logic (Abstraction, Exception Handling)
│   └── ATMException.java     # Custom exception (extends Exception)
└── UI/
    ├── MainDashboard.java    # Main JFrame with CardLayout sidebar
    ├── AnimatedButton.java   # Custom button (Inheritance, Polymorphism)
    ├── LoginPanel.java       # PIN authentication screen
    ├── WithdrawPanel.java    # Cash withdrawal with quick amounts
    ├── DepositPanel.java     # Cash deposit screen
    ├── TransferPanel.java    # Fund transfer between accounts
    ├── BalancePanel.java     # Balance enquiry + mini statement (JTable)
    ├── TransactionHistoryPanel.java  # Full transaction ledger
    └── ChangePinPanel.java   # Secure PIN change
```

---

## OOP Concepts Covered

| Concept | Where Applied |
|---|---|
| Encapsulation | Account.java — private fields with getters/setters |
| Inheritance | AnimatedButton extends JButton |
| Polymorphism | paintComponent() override, ATMException extends Exception |
| Abstraction | ATMService abstracts all business logic from UI |
| Exception Handling | ATMException, try-catch in all service methods |
| Collections | HashMap in AccountDAO, ArrayList in TransactionDAO |
| Generics | List\<Transaction\>, Map\<String, Account\> |
| Enum | Transaction.Type (WITHDRAW, DEPOSIT, TRANSFER_OUT...) |
| Constructor Overloading | Account() and Account(id, name, ...) |

---

## How to Run

```bash
# From root of project
javac -d out Project\ Components/model/*.java \
              Project\ Components/dao/*.java \
              Project\ Components/service/*.java \
              Project\ Components/UI/*.java

java -cp out ui.MainDashboard
```

---

## Demo Accounts

| Name | Account ID | PIN |
|---|---|---|
| Palak | ACC001 | 1111 |
| Bhavya Shree | ACC002 | 2222 |
| Ashvika Singh | ACC003 | 3333 |
| Suzal Sahrawat | ACC004 | 4444 |
| Girdhar Manchanda | ACC005 | 5555 |
| Sajan Deol | ACC006 | 6666 |
