import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * TransactionHistoryPanel - Full transaction ledger with color coding.
 * Demonstrates: JTable customization, Custom cell renderer, Collections.
 * Team: Vision Coders | ATM Simulation System
 */
public class TransactionHistoryPanel extends JPanel {

    private final ATMService   atmService;
    private DefaultTableModel  tableModel;

    public TransactionHistoryPanel(ATMService atmService) {
        this.atmService = atmService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Txn ID", "Date & Time", "Type", "Amount", "Balance", "Note"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(30, 80, 160));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 225, 235));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                String type = (String) t.getValueAt(row, 2);
                if (!sel) {
                    if (type.contains("DEPOSIT") || type.contains("TRANSFER IN")) setForeground(new Color(30, 140, 70));
                    else if (type.contains("WITHDRAW") || type.contains("TRANSFER OUT")) setForeground(new Color(180, 50, 50));
                    else setForeground(new Color(80, 90, 110));
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225)));
        add(scroll, BorderLayout.CENTER);

        AnimatedButton btnRefresh = new AnimatedButton("REFRESH", new Color(52, 73, 94), new Color(41, 128, 185));
        btnRefresh.addActionListener(e -> refresh());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false);
        south.add(btnRefresh);
        add(south, BorderLayout.SOUTH);
    }

    public void refresh() {
        if (!atmService.isLoggedIn()) return;
        try {
            tableModel.setRowCount(0);
            List<Transaction> txns = atmService.getTransactionHistory();
            for (Transaction t : txns) {
                String sign = t.isCredit() ? "+" : (t.getAmount() == 0 ? "" : "-");
                tableModel.addRow(new Object[]{
                    t.getTransactionId(), t.getFormattedTimestamp(),
                    t.getType().toString().replace("_", " "),
                    sign + String.format("%,.2f", t.getAmount()),
                    String.format("%,.2f", t.getBalanceAfter()),
                    t.getNote()
                });
            }
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
