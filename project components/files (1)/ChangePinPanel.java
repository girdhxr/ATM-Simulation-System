import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ChangePinPanel - Secure PIN change screen.
 * Demonstrates: Password fields, Validation, Exception handling in UI.
 * Team: Vision Coders | ATM Simulation System
 */
public class ChangePinPanel extends JPanel {

    private final ATMService atmService;
    private JPasswordField txtCurrent, txtNew, txtConfirm;

    public ChangePinPanel(ATMService atmService) {
        this.atmService = atmService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));
        setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Change PIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(7, 1, 0, 12));
        center.setOpaque(false);

        txtCurrent = makePin(); txtNew = makePin(); txtConfirm = makePin();

        AnimatedButton btnChange = new AnimatedButton("UPDATE PIN",
                new Color(160, 100, 20), new Color(210, 140, 30));
        btnChange.addActionListener(e -> doChange());

        center.add(makeLabel("Current PIN:"));   center.add(txtCurrent);
        center.add(makeLabel("New PIN (4 digits):")); center.add(txtNew);
        center.add(makeLabel("Confirm New PIN:")); center.add(txtConfirm);
        center.add(btnChange);
        add(center, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(80, 90, 110));
        return l;
    }

    private JPasswordField makePin() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private void doChange() {
        String cur  = new String(txtCurrent.getPassword());
        String nw   = new String(txtNew.getPassword());
        String conf = new String(txtConfirm.getPassword());
        try {
            atmService.changePin(cur, nw, conf);
            JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtCurrent.setText(""); txtNew.setText(""); txtConfirm.setText("");
        } catch (ATMException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "PIN Change Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
