import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainDashboard - Main JFrame with sidebar navigation and CardLayout.
 * Entry point of the ATM Simulation System.
 * Demonstrates: CardLayout, BorderLayout, Composition, Swing best practices.
 * Team: Vision Coders | ATM Simulation System
 */
public class MainDashboard extends JFrame {

    private final ATMService atmService = new ATMService();

    private JPanel     contentPanel;
    private CardLayout cardLayout;
    private JPanel     sidebar;
    private JLabel     lblWelcome, lblAccId;

    private LoginPanel              loginPanel;
    private WithdrawPanel           withdrawPanel;
    private DepositPanel            depositPanel;
    private TransferPanel           transferPanel;
    private BalancePanel            balancePanel;
    private TransactionHistoryPanel historyPanel;
    private ChangePinPanel          changePinPanel;

    private AnimatedButton btnWithdraw, btnDeposit, btnTransfer,
                           btnBalance, btnHistory, btnPin, btnLogout;

    public MainDashboard() {
        setTitle("ATM Simulation System — Vision Coders | B5 CCVT");
        setSize(1050, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        buildSidebar();
        buildContent();

        add(sidebar,      BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        showLogin();
    }

    private void buildSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(22, 55, 110));
        sidebar.setPreferredSize(new Dimension(210, 0));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(15, 40, 90));
        header.setBorder(new EmptyBorder(20, 16, 20, 16));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel bankName = new JLabel("VISION BANK");
        bankName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bankName.setForeground(Color.WHITE);
        bankName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel atmLabel = new JLabel("ATM Terminal");
        atmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        atmLabel.setForeground(new Color(150, 180, 230));
        atmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(bankName);
        header.add(Box.createVerticalStrut(4));
        header.add(atmLabel);
        sidebar.add(header);

        // Welcome
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(22, 55, 110));
        welcomePanel.setBorder(new EmptyBorder(14, 16, 14, 16));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        lblWelcome = new JLabel("Welcome");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblWelcome.setForeground(Color.WHITE);

        lblAccId = new JLabel("Please login");
        lblAccId.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAccId.setForeground(new Color(150, 180, 230));

        welcomePanel.add(lblWelcome);
        welcomePanel.add(Box.createVerticalStrut(3));
        welcomePanel.add(lblAccId);
        sidebar.add(welcomePanel);
        sidebar.add(makeDivider());

        Color def  = new Color(30, 70, 140),  hov  = new Color(50, 120, 220);
        Color dang = new Color(140, 40, 40),  hovD = new Color(200, 60, 60);

        btnWithdraw = navBtn("  Withdraw",    def, hov);
        btnDeposit  = navBtn("  Deposit",     def, hov);
        btnTransfer = navBtn("  Transfer",    def, hov);
        btnBalance  = navBtn("  Balance",     def, hov);
        btnHistory  = navBtn("  History",     def, hov);
        btnPin      = navBtn("  Change PIN",  def, hov);
        btnLogout   = navBtn("  Logout",      dang, hovD);

        btnWithdraw.addActionListener(e -> { showCard("withdraw"); withdrawPanel.refresh(); });
        btnDeposit .addActionListener(e -> { showCard("deposit");  depositPanel.refresh(); });
        btnTransfer.addActionListener(e -> { showCard("transfer"); transferPanel.refresh(); });
        btnBalance .addActionListener(e -> { showCard("balance");  balancePanel.refresh(); });
        btnHistory .addActionListener(e -> { showCard("history");  historyPanel.refresh(); });
        btnPin     .addActionListener(e ->   showCard("pin"));
        btnLogout  .addActionListener(e ->   doLogout());

        sidebar.add(btnWithdraw); sidebar.add(btnDeposit);
        sidebar.add(btnTransfer); sidebar.add(btnBalance);
        sidebar.add(btnHistory);  sidebar.add(btnPin);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeDivider());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(10));

        setNavVisible(false);
    }

    private AnimatedButton navBtn(String text, Color def, Color hov) {
        AnimatedButton btn = new AnimatedButton(text, def, hov);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        return btn;
    }

    private JSeparator makeDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(40, 80, 150));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private void buildContent() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        loginPanel     = new LoginPanel(atmService, this::onLoginSuccess);
        withdrawPanel  = new WithdrawPanel(atmService, () -> {});
        depositPanel   = new DepositPanel(atmService,  () -> {});
        transferPanel  = new TransferPanel(atmService, () -> {});
        balancePanel   = new BalancePanel(atmService);
        historyPanel   = new TransactionHistoryPanel(atmService);
        changePinPanel = new ChangePinPanel(atmService);

        contentPanel.add(loginPanel,     "login");
        contentPanel.add(withdrawPanel,  "withdraw");
        contentPanel.add(depositPanel,   "deposit");
        contentPanel.add(transferPanel,  "transfer");
        contentPanel.add(balancePanel,   "balance");
        contentPanel.add(historyPanel,   "history");
        contentPanel.add(changePinPanel, "pin");
    }

    private void showCard(String name) { cardLayout.show(contentPanel, name); }
    private void showLogin()           { showCard("login"); }

    private void setNavVisible(boolean v) {
        btnWithdraw.setVisible(v); btnDeposit.setVisible(v);
        btnTransfer.setVisible(v); btnBalance.setVisible(v);
        btnHistory.setVisible(v);  btnPin.setVisible(v);
        btnLogout.setVisible(v);
    }

    private void onLoginSuccess() {
        lblWelcome.setText("Welcome, " + atmService.getCurrentAccount().getHolderName());
        lblAccId.setText(atmService.getCurrentAccount().getAccountId());
        setNavVisible(true);
        showCard("balance");
        balancePanel.refresh();
    }

    private void doLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        atmService.logout();
        lblWelcome.setText("Welcome");
        lblAccId.setText("Please login");
        setNavVisible(false);
        loginPanel.reset();
        showLogin();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new MainDashboard().setVisible(true);
        });
    }
}
