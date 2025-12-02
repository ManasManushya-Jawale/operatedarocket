package operatedarocket.ui.Clock;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

/**
 * Smooth analog clock rendered with pure Swing.
 * Updates ~100 fps (every 10 ms).
 */
public class JClock extends JComponent {
    private LocalTime time = LocalTime.now();
    private final Timer timer;

    public JClock() {
        setPreferredSize(new Dimension(300, 300));
        setOpaque(false);

        // Update ~100 fps (every 10 ms)
        timer = new Timer(10, e -> {
            time = LocalTime.now();
            repaint();
        });
        timer.start();
    }

    // Optional: allow setting a fixed time (for demo/testing)
    public void setTime(LocalTime t) {
        this.time = t;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h);
            int cx = w / 2;
            int cy = h / 2;
            int radius = (int) (size * 0.45);

            // Clock face
            g2.setColor(new Color(245, 245, 245));
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            g2.setColor(new Color(40, 40, 40));
            g2.setStroke(new BasicStroke(Math.max(2f, size * 0.01f)));
            g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

            // Tick marks
            drawTicks(g2, cx, cy, radius);

            // Compute angles with fractional seconds for smooth sweep
            long millis = System.currentTimeMillis() % 1000;
            double secondFraction = time.getSecond() + millis / 1000.0;

            double hourAngle   = Math.toRadians(((time.getHour() % 12) + time.getMinute() / 60.0) * 30.0);
            double minuteAngle = Math.toRadians((time.getMinute() + secondFraction / 60.0) * 6.0);
            double secondAngle = Math.toRadians(secondFraction * 6.0);

            // Hands
            drawHand(g2, cx, cy, radius * 0.55, hourAngle, Math.max(6f, size * 0.02f), new Color(60, 60, 60));
            drawHand(g2, cx, cy, radius * 0.80, minuteAngle, Math.max(4f, size * 0.015f), new Color(80, 80, 80));
            drawHand(g2, cx, cy, radius * 0.90, secondAngle, Math.max(2f, size * 0.01f), new Color(200, 50, 50));

            // Center cap
            g2.setColor(new Color(40, 40, 40));
            int cap = Math.max(6, (int) (size * 0.03));
            g2.fillOval(cx - cap / 2, cy - cap / 2, cap, cap);

        } finally {
            g2.dispose();
        }
    }

    private void drawTicks(Graphics2D g2, int cx, int cy, int radius) {
        Stroke thin = new BasicStroke(Math.max(1f, radius * 0.01f));
        Stroke thick = new BasicStroke(Math.max(2f, radius * 0.02f));

        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6.0);
            double outer = radius * 0.95;
            double inner = radius * (i % 5 == 0 ? 0.82 : 0.88);

            int x1 = (int) Math.round(cx + outer * Math.sin(angle));
            int y1 = (int) Math.round(cy - outer * Math.cos(angle));
            int x2 = (int) Math.round(cx + inner * Math.sin(angle));
            int y2 = (int) Math.round(cy - inner * Math.cos(angle));

            g2.setStroke(i % 5 == 0 ? thick : thin);
            g2.setColor(new Color(50, 50, 50));
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawHand(Graphics2D g2, int cx, int cy, double length, double angle, float stroke, Color color) {
        double tail = length * 0.15;

        int xTip  = (int) Math.round(cx + length * Math.sin(angle));
        int yTip  = (int) Math.round(cy - length * Math.cos(angle));
        int xTail = (int) Math.round(cx - tail   * Math.sin(angle));
        int yTail = (int) Math.round(cy + tail   * Math.cos(angle));

        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(xTail, yTail, xTip, yTip);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!timer.isRunning()) timer.start();
    }

    @Override
    public void removeNotify() {
        if (timer.isRunning()) timer.stop();
        super.removeNotify();
    }

    // ---- Demo main ----
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Smooth JClock");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new JClock());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
