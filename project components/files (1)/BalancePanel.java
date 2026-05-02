import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * BalancePanel - Balance enquiry and mini-statement screen.
 * Demonstrates: JTable with DefaultTableModel, Collections iteration.
 * Team: Vision Coders | ATM Simulation System
 */
public class BalancePanel extends JPanel {

    private final ATMService   atmService;
    private JLabel             lblBalance;
    private DefaultTableModel  tableModel;

    public BalancePanel(ATMService atmService) {
        this.atmService = atmService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Balance Enquiry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);

        // Balance card
        JPanel balCard = new JPanel(new GridBagLayout());
        balCard.setBackground(new Color(30, 80, 160));
        balCard.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel balText = new JPanel();
        balText.setLayout(new BoxLayout(balText, BoxLayout.Y_AXIS));
        balText.setOpaque(false);

        JLabel balTitle = new JLabel("AVAILABLE BALANCE");
        balTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        balTitle.setForeground(new Color(180, 200, 240));

        lblBalance = new JLabel("Rs ------");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBalance.setForeground(Color.WHITE);

        balText.add(balTitle);
        balText.add(lblBalance);
        balCard.add(balText);

        // Mini statement table
        String[] cols = {"Txn ID", "Date & Time", "Type", "Amount", "Balance"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(220, 230, 245));
        table.setGridColor(new Color(220, 225, 235));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225)));

        AnimatedButton btnRefresh = new AnimatedButton("REFRESH", new Color(52, 73, 94), new Color(41, 128, 185));
        btnRefresh.addActionListener(e -> refresh());

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);
        top.add(balCard, BorderLayout.CENTER);
        top.add(btnRefresh, BorderLayout.EAST);

        center.add(top, BorderLayout.NORTH);
        center.add(new JLabel("Last 5 Transactions:"), BorderLayout.CENTER);
        center.add(scroll, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        if (!atmService.isLoggedIn()) return;
        try {
            double bal = atmService.getBalance();
            lblBalance.setText("Rs " + String.format("%,.2f", bal));
            tableModel.setRowCount(0);
            List<Transaction> txns = atmService.getMiniStatement();
            for (Transaction t : txns) {
                String sign = t.isCredit() ? "+" : "-";
                tableModel.addRow(new Object[]{
                    t.getTransactionId(), t.getFormattedTimestamp(),
                    t.getType().toString().replace("_", " "),
                    sign + String.format("%,.2f", t.getAmount()),
                    String.format("%,.2f", t.getBalanceAfter())
                });
            }
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
