import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccountDAO - Data Access Object for Account operations.
 * Demonstrates: Abstraction, Collections (HashMap), in-memory data store.
 * Team: Vision Coders | ATM Simulation System
 */
public class AccountDAO {

    private static final Map<String, Account> accountStore = new HashMap<>();

    static {
        accountStore.put("ACC001", new Account("ACC001", "Palak",             "590013843", "1111", 50000.00));
        accountStore.put("ACC002", new Account("ACC002", "Bhavya Shree",      "590014888", "2222", 75000.00));
        accountStore.put("ACC003", new Account("ACC003", "Ashvika Singh",     "590014516", "3333", 30000.00));
        accountStore.put("ACC004", new Account("ACC004", "Suzal Sahrawat",    "590015312", "4444", 60000.00));
        accountStore.put("ACC005", new Account("ACC005", "Girdhar Manchanda", "590012105", "5555", 45000.00));
        accountStore.put("ACC006", new Account("ACC006", "Sajan Deol",        "590013805", "6666", 90000.00));
    }

    public Account findById(String accountId) {
        return accountStore.get(accountId.toUpperCase());
    }

    public void update(Account account) {
        accountStore.put(account.getAccountId(), account);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accountStore.values());
    }

    public boolean exists(String accountId) {
        return accountStore.containsKey(accountId.toUpperCase());
    }
}
