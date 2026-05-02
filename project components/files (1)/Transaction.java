import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction - Represents a single ATM transaction record.
 * Demonstrates: Encapsulation, Enum, Immutable design pattern.
 * Team: Vision Coders | ATM Simulation System
 */
public class Transaction {

    public enum Type {
        WITHDRAW, DEPOSIT, TRANSFER_OUT, TRANSFER_IN, PIN_CHANGE, BALANCE_ENQUIRY
    }

    private final String        transactionId;
    private final String        accountId;
    private final Type          type;
    private final double        amount;
    private final double        balanceAfter;
    private final String        note;
    private final LocalDateTime timestamp;

    private static int counter = 1000;

    public Transaction(String accountId, Type type, double amount, double balanceAfter, String note) {
        this.transactionId = "TXN" + (++counter);
        this.accountId     = accountId;
        this.type          = type;
        this.amount        = amount;
        this.balanceAfter  = balanceAfter;
        this.note          = note;
        this.timestamp     = LocalDateTime.now();
    }

    public String        getTransactionId() { return transactionId; }
    public String        getAccountId()     { return accountId; }
    public Type          getType()          { return type; }
    public double        getAmount()        { return amount; }
    public double        getBalanceAfter()  { return balanceAfter; }
    public String        getNote()          { return note; }
    public LocalDateTime getTimestamp()     { return timestamp; }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public boolean isCredit() {
        return type == Type.DEPOSIT || type == Type.TRANSFER_IN;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %.2f | Bal: %.2f",
                transactionId, getFormattedTimestamp(), type, amount, balanceAfter);
    }
}
