import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * AnimatedButton - Custom Swing button with smooth hover color animation.
 * Demonstrates: Inheritance (extends JButton), Polymorphism (paintComponent override).
 * Team: Vision Coders | ATM Simulation System
 */
public class AnimatedButton extends JButton {

    private Color defaultColor;
    private Color hoverColor;
    private Color currentColor;
    private Timer animTimer;
    private float blendRatio = 0.0f;
    private boolean isHovering = false;

    public AnimatedButton(String text, Color defaultColor, Color hoverColor) {
        super(text);
        this.defaultColor = defaultColor;
        this.hoverColor   = hoverColor;
        this.currentColor = defaultColor;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        animTimer = new Timer(15, e -> animateColor());

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { isHovering = true;  animTimer.start(); }
            @Override public void mouseExited(MouseEvent e)  { isHovering = false; animTimer.start(); }
        });
    }

    private void animateColor() {
        if (isHovering && blendRatio < 1.0f)       blendRatio += 0.1f;
        else if (!isHovering && blendRatio > 0.0f) blendRatio -= 0.1f;
        else { animTimer.stop(); }

        blendRatio = Math.max(0.0f, Math.min(1.0f, blendRatio));

        int r = (int)(defaultColor.getRed()   + (hoverColor.getRed()   - defaultColor.getRed())   * blendRatio);
        int g = (int)(defaultColor.getGreen() + (hoverColor.getGreen() - defaultColor.getGreen()) * blendRatio);
        int b = (int)(defaultColor.getBlue()  + (hoverColor.getBlue()  - defaultColor.getBlue())  * blendRatio);

        currentColor = new Color(r, g, b);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        super.paintComponent(g);
        g2.dispose();
    }
}
