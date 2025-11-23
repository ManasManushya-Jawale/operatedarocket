package operatedarocket.ui;

import javax.swing.*;

import operatedarocket.OperateDaRocketApplication;
import operatedarocket.ResourceLoader;
import operatedarocket.LotusOS.HomeScreen;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AppFrame extends JFrame {
    // UI
    public JPanel topBar;
    public JPanel content;

    // Controls
    public JButton maximize;
    public JButton close;
    public JButton minimize;

    // Resizers
    public JPanel bottomResizer;
    public JPanel rightResizer;
    public JPanel cornerResizer; // bottom-right corner

    // Drag state for moving window
    public int dragStartScreenX = 0, dragStartScreenY = 0;
    public int dragStartWindowX = 0, dragStartWindowY = 0;

    // Resize state
    public int resizeStartScreenX = 0, resizeStartScreenY = 0;
    public int resizeStartW = 0, resizeStartH = 0;

    // Flags
    public boolean maximized = false;

    // Style
    public static final int RADIUS = 20;
    public static final Dimension MIN_SIZE = new Dimension(360, 240);

    public AppFrame(String title) {
        // Frame setup
        setAlwaysOnTop(true);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(MIN_SIZE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Top bar
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(50, 50, 50));

        // Left: title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ResourceLoader.font("/fonts/JetBrainsMono-Bold.ttf").deriveFont(16f));
        titleLabel.setForeground(Color.white);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        topBar.add(titleLabel, BorderLayout.WEST);

        // Right: window buttons
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        controls.setOpaque(false);

        minimize = new JButton("_");
        minimize.setFocusable(false);
        minimize.addActionListener(e -> onMinimize());

        maximize = new JButton("□");
        maximize.setFocusable(false);
        maximize.addActionListener(e -> onMaximize());

        close = new JButton("X");
        close.setFocusable(false);
        close.addActionListener(e -> onClose());

        controls.add(minimize);
        controls.add(maximize);
        controls.add(close);
        topBar.add(controls, BorderLayout.EAST);

        // Drag to move (topBar only — avoids conflict with content)
        addMoveDragBehavior(topBar);

        // Content
        content = new JPanel(new BorderLayout());

        // Resizers
        bottomResizer = new JPanel();
        bottomResizer.setPreferredSize(new Dimension(0, 6));
        bottomResizer.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        addResizeBehavior(bottomResizer, true, false);

        rightResizer = new JPanel();
        rightResizer.setPreferredSize(new Dimension(6, 0));
        rightResizer.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        addResizeBehavior(rightResizer, false, true);

        cornerResizer = new JPanel();
        cornerResizer.setPreferredSize(new Dimension(8, 8));
        cornerResizer.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        addResizeBehavior(cornerResizer, true, true);

        // Corner resizer sits inside a small container at SE corner
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(bottomResizer, BorderLayout.CENTER);

        JPanel southEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        southEast.setOpaque(false);
        southEast.add(cornerResizer);
        south.add(southEast, BorderLayout.EAST);

        // Add components
        add(topBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        add(rightResizer, BorderLayout.EAST);

        // Round shape
        SwingUtilities.invokeLater(this::applyRoundedShape);
    }

    // Public API
    public Component addContent(Component component) {
        return content.add(component);
    }

    // Public API
    public Component addContent(Component component, Object constraint) {
        content.add(component, constraint);
        return component;
    }

    // Actions
    private void onClose() {
        dispose();
    }

    private void onMinimize() {
        setState(Frame.ICONIFIED);
    }

    private void onMaximize() {

        if (maximized) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            gd.setFullScreenWindow(OperateDaRocketApplication.frame); // Exit fullscreen
            setSize(600, 400);
            centerOnScreen();
            maximize.setText("□");
            applyRoundedShape();

        } else {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            gd.setFullScreenWindow(this);
            maximize.setText("⊙");

        }
        maximized = !maximized;
    }

    // Helpers
    private void centerOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - getWidth()) / 2;
        int y = (screen.height - getHeight()) / 2;
        setLocation(Math.max(0, x), Math.max(0, y));
    }

    private void applyRoundedShape() {
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
    }

    private void addMoveDragBehavior(Component c) {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartScreenX = e.getXOnScreen();
                dragStartScreenY = e.getYOnScreen();
                dragStartWindowX = getX();
                dragStartWindowY = getY();
            }
        });
        c.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (maximized) return; // disable move while maximized
                int dx = e.getXOnScreen() - dragStartScreenX;
                int dy = e.getYOnScreen() - dragStartScreenY;
                setLocation(dragStartWindowX + dx, dragStartWindowY + dy);
            }
        });
    }

    private void addResizeBehavior(JComponent resizer, boolean resizeY, boolean resizeX) {
        resizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                resizeStartScreenX = e.getXOnScreen();
                resizeStartScreenY = e.getYOnScreen();
                resizeStartW = getWidth();
                resizeStartH = getHeight();
            }
        });
        resizer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (maximized) return; // disable resize while maximized

                int dx = e.getXOnScreen() - resizeStartScreenX;
                int dy = e.getXOnScreen(); // placeholder to avoid unused warning (we use both)
                dy = e.getYOnScreen() - resizeStartScreenY;

                int newW = resizeStartW;
                int newH = resizeStartH;

                if (resizeX) newW = clamp(resizeStartW + dx, MIN_SIZE.width, Integer.MAX_VALUE);
                if (resizeY) newH = clamp(resizeStartH + dy, MIN_SIZE.height, Integer.MAX_VALUE);

                // Apply new size
                setSize(newW, newH);

                // Keep rounded shape in sync unless maximized
                applyRoundedShape();
            }
        });
    }

    private int clamp(int v, int min, int max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    // Keep shape synced whenever size changes programmatically
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (!maximized) {
            applyRoundedShape();
        }
    }
}
