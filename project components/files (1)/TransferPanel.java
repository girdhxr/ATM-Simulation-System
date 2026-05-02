import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TransferPanel - Fund transfer between accounts.
 * Demonstrates: Form validation, Service layer calls, Exception handling in UI.
 * Team: Vision Coders | ATM Simulation System
 */
public class TransferPanel extends JPanel {

    private final ATMService atmService;
    private final Runnable   onSuccess;
    private JTextField txtToAccount, txtAmount, txtRemark;
    private JLabel     lblBalance;

    public TransferPanel(ATMService atmService, Runnable onSuccess) {
        this.atmService = atmService;
        this.onSuccess  = onSuccess;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Fund Transfer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(8, 1, 0, 12));
        center.setOpaque(false);

        lblBalance = new JLabel("Available Balance: ------");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalance.setForeground(new Color(40, 160, 80));

        JLabel lblTo = makeLabel("Recipient Account ID:");
        txtToAccount = makeField();

        JLabel lblAmt = makeLabel("Amount:");
        txtAmount = makeField();

        JLabel lblRemark = makeLabel("Remarks (optional):");
        txtRemark = makeField();

        AnimatedButton btnTransfer = new AnimatedButton("TRANSFER FUNDS",
                new Color(100, 50, 160), new Color(140, 80, 210));
        btnTransfer.addActionListener(e -> doTransfer());

        center.add(lblBalance); center.add(lblTo);   center.add(txtToAccount);
        center.add(lblAmt);     center.add(txtAmount); center.add(lblRemark);
        center.add(txtRemark);  center.add(btnTransfer);
        add(center, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(80, 90, 110));
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI Mono", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    public void refresh() {
        if (atmService.isLoggedIn())
            lblBalance.setText("Available Balance: Rs " +
                    String.format("%,.2f", atmService.getCurrentAccount().getBalance()));
    }

    private void doTransfer() {
        String toId   = txtToAccount.getText().trim().toUpperCase();
        String input  = txtAmount.getText().trim();
        String remark = txtRemark.getText().trim();
        if (toId.isEmpty() || input.isEmpty()) { JOptionPane.showMessageDialog(this, "Account ID and amount are required.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
        try {
            double amount = Double.parseDouble(input);
            atmService.transfer(toId, amount, remark);
            JOptionPane.showMessageDialog(this, String.format("Rs %,.2f transferred to %s successfully!", amount, toId), "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
            txtToAccount.setText(""); txtAmount.setText(""); txtRemark.setText(""); onSuccess.run(); refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transfer Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
