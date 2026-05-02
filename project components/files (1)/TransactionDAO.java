import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransactionDAO - Data Access Object for Transaction records.
 * Demonstrates: Collections (HashMap + ArrayList), Generics.
 * Team: Vision Coders | ATM Simulation System
 */
public class TransactionDAO {

    private static final Map<String, List<Transaction>> txnStore = new HashMap<>();

    public void save(Transaction txn) {
        txnStore.computeIfAbsent(txn.getAccountId(), k -> new ArrayList<>())
                .add(0, txn);
    }

    public List<Transaction> findByAccountId(String accountId) {
        return txnStore.getOrDefault(accountId, new ArrayList<>());
    }

    public List<Transaction> findRecent(String accountId, int limit) {
        List<Transaction> all = findByAccountId(accountId);
        return all.subList(0, Math.min(limit, all.size()));
    }

    public void clearByAccountId(String accountId) {
        txnStore.remove(accountId);
    }
}
