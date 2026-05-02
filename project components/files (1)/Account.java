/**
 * Account - Core model class representing a bank account.
 * Demonstrates: Encapsulation, Constructor Overloading, toString() override.
 * Team: Vision Coders | ATM Simulation System
 */
public class Account {

    private String accountId;
    private String holderName;
    private String sapId;
    private String pin;
    private double balance;
    private boolean locked;
    private int failedAttempts;

    public Account() {
        this("UNKNOWN", "Unknown", "000000000", "0000", 0.0);
    }

    public Account(String accountId, String holderName, String sapId, String pin, double balance) {
        this.accountId      = accountId;
        this.holderName     = holderName;
        this.sapId          = sapId;
        this.pin            = pin;
        this.balance        = balance;
        this.locked         = false;
        this.failedAttempts = 0;
    }

    public String  getAccountId()    { return accountId; }
    public String  getHolderName()   { return holderName; }
    public String  getSapId()        { return sapId; }
    public String  getPin()          { return pin; }
    public double  getBalance()      { return balance; }
    public boolean isLocked()        { return locked; }
    public int     getFailedAttempts(){ return failedAttempts; }

    public void setPin(String pin)           { this.pin = pin; }
    public void setBalance(double balance)   { this.balance = balance; }
    public void setLocked(boolean locked)    { this.locked = locked; }
    public void setFailedAttempts(int n)     { this.failedAttempts = n; }

    public void credit(double amount) { this.balance += amount; }
    public void debit(double amount)  { this.balance -= amount; }

    public boolean validatePin(String inputPin) { return this.pin.equals(inputPin); }

    @Override
    public String toString() {
        return String.format("Account[id=%s, holder=%s, balance=%.2f]", accountId, holderName, balance);
    }
}
