import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * LoginPanel - PIN-based authentication screen.
 * Demonstrates: Event Handling, Swing layout, Input validation.
 * Team: Vision Coders | ATM Simulation System
 */
public class LoginPanel extends JPanel {

    private final ATMService atmService;
    private final Runnable   onLoginSuccess;

    private JTextField     txtAccountId;
    private JPasswordField txtPin;
    private JLabel         lblStatus;

    public LoginPanel(ATMService atmService, Runnable onLoginSuccess) {
        this.atmService      = atmService;
        this.onLoginSuccess  = onLoginSuccess;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                new EmptyBorder(40, 50, 40, 50)));
        card.setPreferredSize(new Dimension(380, 430));

        JLabel title = new JLabel("VISION BANK ATM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 80, 160));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Enter Account ID & PIN");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(120, 130, 150));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel lblAcc = new JLabel("Account ID");
        lblAcc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAcc.setForeground(new Color(80, 90, 110));
        lblAcc.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtAccountId = new JTextField();
        txtAccountId.setFont(new Font("Segoe UI Mono", Font.PLAIN, 14));
        txtAccountId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtAccountId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(4, 10, 4, 10)));

        JLabel lblPinLbl = new JLabel("PIN");
        lblPinLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPinLbl.setForeground(new Color(80, 90, 110));
        lblPinLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPin = new JPasswordField();
        txtPin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(4, 10, 4, 10)));
        txtPin.addActionListener(e -> doLogin());

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(200, 60, 60));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        AnimatedButton btnLogin = new AnimatedButton("  AUTHENTICATE  ",
                new Color(30, 80, 160), new Color(50, 120, 220));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.addActionListener(e -> doLogin());

        JLabel hint = new JLabel("<html><center>Demo: ACC001/1111 &nbsp;|&nbsp; ACC005/5555 (Girdhar)</center></html>");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(160, 170, 190));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(lblAcc);
        card.add(Box.createVerticalStrut(6));
        card.add(txtAccountId);
        card.add(Box.createVerticalStrut(14));
        card.add(lblPinLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(txtPin);
        card.add(Box.createVerticalStrut(10));
        card.add(lblStatus);
        card.add(Box.createVerticalStrut(10));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        add(card);
    }

    private void doLogin() {
        String accountId = txtAccountId.getText().trim().toUpperCase();
        String pin       = new String(txtPin.getPassword()).trim();
        if (accountId.isEmpty() || pin.isEmpty()) {
            lblStatus.setText("Please enter Account ID and PIN.");
            return;
        }
        try {
            atmService.login(accountId, pin);
            txtAccountId.setText(""); txtPin.setText(""); lblStatus.setText(" ");
            onLoginSuccess.run();
        } catch (ATMException ex) {
            lblStatus.setText(ex.getMessage());
            txtPin.setText("");
        }
    }

    public void reset() {
        txtAccountId.setText(""); txtPin.setText(""); lblStatus.setText(" ");
    }
}
