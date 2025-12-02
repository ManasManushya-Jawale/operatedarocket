package operatedarocket.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AppFrame extends JFrame {

    private final JPanel topBar;
    public final JPanel content;
    private final JButton close;
    private final JButton maximize;
    private final JButton minimize;

    private final JPanel topResizer, bottomResizer, leftResizer, rightResizer;

    private boolean maximized = false;
    private static final int RADIUS = 15;
    private Point initialClick;

    public AppFrame(String title) {
        super(title);

        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top bar
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(50, 50, 50));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        controls.setOpaque(false);

        minimize = new JButton("_");
        maximize = new JButton("□");
        close = new JButton("X");

        controls.add(minimize);
        controls.add(maximize);
        controls.add(close);
        topBar.add(controls, BorderLayout.EAST);

        // Content area
        content = new JPanel(new BorderLayout());
        add(content, BorderLayout.CENTER);

        // Button actions
        close.addActionListener(e -> dispose());
        maximize.addActionListener(e -> onMaximize());
        minimize.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Rounded corners update on resize
        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                if (!maximized) applyRoundedShape();
            }
        });

        // Dragging logic
        topBar.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (maximized) {
                    onMaximize();
                    try {
                        Robot robot = new Robot();
                        robot.mouseMove(topBar.getX(), topBar.getY());
                    } catch (AWTException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                initialClick = e.getPoint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                initialClick = null;
            }
        });
        topBar.addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (initialClick != null && !maximized) {
                    int thisX = getLocation().x;
                    int thisY = getLocation().y;
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;
                    setLocation(thisX + xMoved, thisY + yMoved);
                }
            }
        });

        bottomResizer = new JPanel();
        rightResizer = new JPanel();
        leftResizer = new JPanel();
        topResizer = new JPanel();

        topResizer.setBackground(new Color(50, 50, 50));
        int RESIZE_MARGIN = 6;

        topResizer.setPreferredSize(new Dimension(0, RESIZE_MARGIN));
        leftResizer.setPreferredSize(new Dimension(RESIZE_MARGIN, 0));
        rightResizer.setPreferredSize(new Dimension(RESIZE_MARGIN, 0));
        bottomResizer.setPreferredSize(new Dimension(0, RESIZE_MARGIN));

        topResizer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle bounds = getBounds();
                int dy = e.getYOnScreen();
                int newH = bounds.height + bounds.y - dy;
                if (newH > getMinimumSize().height) {
                    bounds.height = newH;
                    bounds.y = dy;
                    setBounds(bounds);
                    applyRoundedShape();
                }
            }
        });

        bottomResizer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle bounds = getBounds();
                int dy = e.getYOnScreen();
                int newH = dy - bounds.y;
                if (newH > getMinimumSize().height) {
                    bounds.height = newH;
                    setBounds(bounds);
                    applyRoundedShape();
                }
            }
        });

        leftResizer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle bounds = getBounds();
                int dx = e.getXOnScreen();
                int newW = bounds.width + bounds.x - dx;
                if (newW > getMinimumSize().width) {
                    bounds.width = newW;
                    bounds.x = dx;
                    setBounds(bounds);
                    applyRoundedShape();
                }
            }
        });

        rightResizer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle bounds = getBounds();
                int dx = e.getXOnScreen();
                int newW = dx - bounds.x;
                if (newW > getMinimumSize().width) {
                    bounds.width = newW;
                    setBounds(bounds);
                    applyRoundedShape();
                }
            }
        });

        add(new JPanel()
        {
            {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                add(topResizer);
                add(topBar);
            }
        }, BorderLayout.NORTH);

        add(bottomResizer, BorderLayout.SOUTH);
        add(rightResizer, BorderLayout.EAST);
        add(leftResizer, BorderLayout.WEST);

        // Force the window to the top of the Z-order
        setAlwaysOnTop(true);
        SwingUtilities.invokeLater(this::applyRoundedShape);
        setVisible(true);
    }

    public void addContent(Component comp, Object constraint) {
        content.add(comp, constraint);
        content.revalidate();
        content.repaint();
    }

    public void addContent(Component comp) {
        content.add(comp);
        content.revalidate();
        content.repaint();
    }
    private void onMaximize() {
        if (maximized) {
            // Restore to default size
            setBounds(100, 100, 900, 600);
            maximize.setText("□");
            applyRoundedShape();
        } else {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            Rectangle bounds = gd.getDefaultConfiguration().getBounds();

            // Leave space for your custom top bar
            int topBarHeight = topBar.getPreferredSize().height - 10; // dynamic height
            int margin = 0; // optional extra margin

            setBounds(bounds.x + margin,
                    bounds.y + topBarHeight,
                    bounds.width,
                    bounds.height - topBarHeight - margin);

            setShape(null); // remove rounded corners when maximized
            maximize.setText("⊙");
        }
        maximized = !maximized;
        revalidate();
        repaint();
    }


    private void applyRoundedShape() {
        setShape(new RoundRectangle2D.Double(
                0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
    }
}
