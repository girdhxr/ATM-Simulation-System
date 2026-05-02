import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * DepositPanel - Cash deposit screen.
 * Demonstrates: Swing form design, Input validation, Exception handling.
 * Team: Vision Coders | ATM Simulation System
 */
public class DepositPanel extends JPanel {

    private final ATMService atmService;
    private final Runnable   onSuccess;
    private JTextField txtAmount, txtNote;
    private JLabel     lblBalance;

    public DepositPanel(ATMService atmService, Runnable onSuccess) {
        this.atmService = atmService;
        this.onSuccess  = onSuccess;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Cash Deposit");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(6, 1, 0, 14));
        center.setOpaque(false);

        lblBalance = new JLabel("Current Balance: ------");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalance.setForeground(new Color(40, 160, 80));

        JLabel lblAmt = new JLabel("Deposit Amount:");
        lblAmt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAmt.setForeground(new Color(80, 90, 110));

        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI Mono", Font.PLAIN, 16));
        txtAmount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(6, 10, 6, 10)));

        JLabel lblNote = new JLabel("Note / Source (optional):");
        lblNote.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNote.setForeground(new Color(80, 90, 110));

        txtNote = new JTextField();
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNote.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(6, 10, 6, 10)));

        AnimatedButton btnDeposit = new AnimatedButton("DEPOSIT CASH",
                new Color(39, 130, 80), new Color(46, 180, 100));
        btnDeposit.addActionListener(e -> doDeposit());

        center.add(lblBalance); center.add(lblAmt); center.add(txtAmount);
        center.add(lblNote);    center.add(txtNote); center.add(btnDeposit);
        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        if (atmService.isLoggedIn())
            lblBalance.setText("Current Balance: Rs " +
                    String.format("%,.2f", atmService.getCurrentAccount().getBalance()));
    }

    private void doDeposit() {
        String input = txtAmount.getText().trim();
        String note  = txtNote.getText().trim();
        if (input.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter an amount.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
        try {
            double amount = Double.parseDouble(input);
            atmService.deposit(amount, note);
            JOptionPane.showMessageDialog(this, String.format("Rs %,.2f deposited successfully!", amount), "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
            txtAmount.setText(""); txtNote.setText(""); onSuccess.run(); refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
