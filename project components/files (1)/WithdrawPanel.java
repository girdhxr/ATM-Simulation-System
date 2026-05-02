import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * WithdrawPanel - Cash withdrawal with quick amounts and custom input.
 * Demonstrates: Event Handling, GridLayout, Input Validation.
 * Team: Vision Coders | ATM Simulation System
 */
public class WithdrawPanel extends JPanel {

    private final ATMService atmService;
    private final Runnable   onSuccess;
    private JTextField txtAmount;
    private JLabel     lblBalance;

    public WithdrawPanel(ATMService atmService, Runnable onSuccess) {
        this.atmService = atmService;
        this.onSuccess  = onSuccess;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Cash Withdrawal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        lblBalance = new JLabel("Available Balance: ------");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalance.setForeground(new Color(40, 160, 80));

        JLabel quickLabel = new JLabel("Quick Withdraw:");
        quickLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        quickLabel.setForeground(new Color(80, 90, 110));

        int[] quickAmounts = {500, 1000, 2000, 5000, 10000, 20000};
        JPanel quickPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        quickPanel.setOpaque(false);
        quickPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        for (int amt : quickAmounts) {
            AnimatedButton btn = new AnimatedButton("Rs " + String.format("%,d", amt),
                    new Color(52, 73, 94), new Color(41, 128, 185));
            btn.addActionListener(e -> { txtAmount.setText(String.valueOf(amt)); doWithdraw(); });
            quickPanel.add(btn);
        }

        JLabel customLabel = new JLabel("Custom Amount:");
        customLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        customLabel.setForeground(new Color(80, 90, 110));

        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI Mono", Font.PLAIN, 16));
        txtAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtAmount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(4, 10, 4, 10)));
        txtAmount.addActionListener(e -> doWithdraw());

        AnimatedButton btnWithdraw = new AnimatedButton("WITHDRAW CASH",
                new Color(180, 50, 50), new Color(220, 70, 70));
        btnWithdraw.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnWithdraw.addActionListener(e -> doWithdraw());

        center.add(lblBalance);
        center.add(Box.createVerticalStrut(20));
        center.add(quickLabel);
        center.add(Box.createVerticalStrut(8));
        center.add(quickPanel);
        center.add(Box.createVerticalStrut(20));
        center.add(customLabel);
        center.add(Box.createVerticalStrut(8));
        center.add(txtAmount);
        center.add(Box.createVerticalStrut(14));
        center.add(btnWithdraw);

        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        if (atmService.isLoggedIn())
            lblBalance.setText("Available Balance: Rs " +
                    String.format("%,.2f", atmService.getCurrentAccount().getBalance()));
    }

    private void doWithdraw() {
        String input = txtAmount.getText().trim();
        if (input.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter an amount.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
        try {
            double amount = Double.parseDouble(input);
            atmService.withdraw(amount);
            JOptionPane.showMessageDialog(this, String.format("Rs %,.2f withdrawn successfully!", amount), "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
            txtAmount.setText(""); onSuccess.run(); refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
