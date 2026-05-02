import java.util.List;

/**
 * ATMService - Core business logic layer for all ATM operations.
 * Demonstrates: Abstraction, Exception Handling, Service-DAO separation.
 * Team: Vision Coders | ATM Simulation System
 */
public class ATMService {

    private static final double MAX_WITHDRAW = 50000.0;
    private static final double MAX_DEPOSIT  = 200000.0;
    private static final int    MAX_ATTEMPTS = 3;

    private final AccountDAO     accountDAO;
    private final TransactionDAO txnDAO;
    private Account currentAccount = null;

    public ATMService() {
        this.accountDAO = new AccountDAO();
        this.txnDAO     = new TransactionDAO();
    }

    // ── Authentication ───────────────────────────────────────

    public boolean login(String accountId, String pin) throws ATMException {
        Account acc = accountDAO.findById(accountId);
        if (acc == null)    throw new ATMException("Account not found: " + accountId);
        if (acc.isLocked()) throw new ATMException("Account is locked. Contact bank support.");

        if (!acc.validatePin(pin)) {
            acc.setFailedAttempts(acc.getFailedAttempts() + 1);
            if (acc.getFailedAttempts() >= MAX_ATTEMPTS) {
                acc.setLocked(true);
                accountDAO.update(acc);
                throw new ATMException("Account locked after " + MAX_ATTEMPTS + " failed attempts.");
            }
            accountDAO.update(acc);
            int remaining = MAX_ATTEMPTS - acc.getFailedAttempts();
            throw new ATMException("Incorrect PIN. " + remaining + " attempt(s) remaining.");
        }

        acc.setFailedAttempts(0);
        accountDAO.update(acc);
        this.currentAccount = acc;
        return true;
    }

    public void    logout()           { this.currentAccount = null; }
    public Account getCurrentAccount(){ return currentAccount; }
    public boolean isLoggedIn()       { return currentAccount != null; }

    // ── Operations ───────────────────────────────────────────

    public void withdraw(double amount) throws ATMException {
        ensureLoggedIn();
        if (amount <= 0)               throw new ATMException("Amount must be greater than zero.");
        if (amount % 100 != 0)         throw new ATMException("Amount must be in multiples of 100.");
        if (amount > MAX_WITHDRAW)     throw new ATMException("Max withdrawal per transaction: " + (int)MAX_WITHDRAW);
        Account acc = accountDAO.findById(currentAccount.getAccountId());
        if (amount > acc.getBalance()) throw new ATMException("Insufficient balance. Available: " + acc.getBalance());

        acc.debit(amount);
        accountDAO.update(acc);
        currentAccount = acc;
        txnDAO.save(new Transaction(acc.getAccountId(), Transaction.Type.WITHDRAW, amount, acc.getBalance(), "Cash Withdrawal"));
    }

    public void deposit(double amount, String note) throws ATMException {
        ensureLoggedIn();
        if (amount <= 0)          throw new ATMException("Amount must be greater than zero.");
        if (amount > MAX_DEPOSIT) throw new ATMException("Max deposit per transaction: " + (int)MAX_DEPOSIT);

        Account acc = accountDAO.findById(currentAccount.getAccountId());
        acc.credit(amount);
        accountDAO.update(acc);
        currentAccount = acc;
        String txnNote = (note != null && !note.trim().isEmpty()) ? note : "Cash Deposit";
        txnDAO.save(new Transaction(acc.getAccountId(), Transaction.Type.DEPOSIT, amount, acc.getBalance(), txnNote));
    }

    public void transfer(String toAccountId, double amount, String remark) throws ATMException {
        ensureLoggedIn();
        if (amount <= 0)                                          throw new ATMException("Amount must be greater than zero.");
        if (toAccountId.equalsIgnoreCase(currentAccount.getAccountId())) throw new ATMException("Cannot transfer to your own account.");
        if (!accountDAO.exists(toAccountId))                      throw new ATMException("Recipient account not found: " + toAccountId);

        Account from = accountDAO.findById(currentAccount.getAccountId());
        Account to   = accountDAO.findById(toAccountId);
        if (amount > from.getBalance()) throw new ATMException("Insufficient balance.");

        from.debit(amount);
        to.credit(amount);
        accountDAO.update(from);
        accountDAO.update(to);
        currentAccount = from;

        String note = (remark != null && !remark.isEmpty()) ? remark : "Fund Transfer";
        txnDAO.save(new Transaction(from.getAccountId(), Transaction.Type.TRANSFER_OUT, amount, from.getBalance(), "To "   + to.getHolderName()   + " (" + toAccountId + ") | " + note));
        txnDAO.save(new Transaction(to.getAccountId(),   Transaction.Type.TRANSFER_IN,  amount, to.getBalance(),   "From " + from.getHolderName() + " (" + from.getAccountId() + ") | " + note));
    }

    public void changePin(String currentPin, String newPin, String confirmPin) throws ATMException {
        ensureLoggedIn();
        Account acc = accountDAO.findById(currentAccount.getAccountId());
        if (!acc.validatePin(currentPin))                    throw new ATMException("Current PIN is incorrect.");
        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) throw new ATMException("New PIN must be exactly 4 digits.");
        if (!newPin.equals(confirmPin))                      throw new ATMException("New PIN and confirm PIN do not match.");
        if (newPin.equals(currentPin))                       throw new ATMException("New PIN cannot be same as current PIN.");

        acc.setPin(newPin);
        accountDAO.update(acc);
        currentAccount = acc;
        txnDAO.save(new Transaction(acc.getAccountId(), Transaction.Type.PIN_CHANGE, 0, acc.getBalance(), "PIN Changed Successfully"));
    }

    public double getBalance() throws ATMException {
        ensureLoggedIn();
        Account acc = accountDAO.findById(currentAccount.getAccountId());
        txnDAO.save(new Transaction(acc.getAccountId(), Transaction.Type.BALANCE_ENQUIRY, 0, acc.getBalance(), "Balance Enquiry"));
        return acc.getBalance();
    }

    public List<Transaction> getTransactionHistory() throws ATMException {
        ensureLoggedIn();
        return txnDAO.findByAccountId(currentAccount.getAccountId());
    }

    public List<Transaction> getMiniStatement() throws ATMException {
        ensureLoggedIn();
        return txnDAO.findRecent(currentAccount.getAccountId(), 5);
    }

    public List<Account> getAllAccounts() { return accountDAO.findAll(); }

    private void ensureLoggedIn() throws ATMException {
        if (!isLoggedIn()) throw new ATMException("No active session. Please login first.");
    }
}
