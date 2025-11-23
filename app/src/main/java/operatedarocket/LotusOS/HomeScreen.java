package operatedarocket.LotusOS;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import operatedarocket.ResourceLoader;
import operatedarocket.apps.AppsMenu.AppsMenu;
import operatedarocket.apps.FlowerMail.FlowerMailBackend;
import operatedarocket.ui.DesktopIcon;
import operatedarocket.util.Apps.AppRejistry;
import operatedarocket.Utilities;

/**
 * Improved HomeScreen:
 * - Proper content sizing (accounts for dock width)
 * - Scaled wallpaper that updates on resize
 * - Single method to create dock icons (no duplicate creation)
 * - App frames launched on EDT and not AlwaysOnTop by default
 * - Small hover effect on icons
 */
public class HomeScreen extends JPanel {
    private static final int DOCK_WIDTH = 80;
    public final JPanel dockPanel;
    private final JLayeredPane contentPane;
    private final JPanel doorsPanel;
    private final JLabel backgroundLabel;
    boolean showAppsMenu = false;

    private final JFrame appsMenuFrame = new AppsMenu();

    public HomeScreen() throws Exception {
        super(new BorderLayout());
        FlowerMailBackend.init();

        // Create UI parts
        dockPanel = createDockPanel();
        contentPane = createContentPane();
        doorsPanel = new JPanel(null);
        doorsPanel.setOpaque(false);
        contentPane.add(doorsPanel, Integer.valueOf(1));

        // background label stored so we can update its icon on resize
        backgroundLabel = new JLabel();
        contentPane.add(backgroundLabel, Integer.valueOf(0));

        // add to layout
        add(dockPanel, BorderLayout.WEST);
        add(contentPane, BorderLayout.CENTER);

        // compute initial sizes and set background
        revalidate();
        SwingUtilities.invokeLater(this::updateSizesAndBackground);

        // listen for resize so wallpaper and cpSize update
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizesAndBackground();
            }
        });
    }

    /**
     * Build dock with DesktopIcons from AppRejistry list.
     */
    private JPanel createDockPanel() throws Exception {
        JPanel dock = new JPanel();
        dock.setLayout(new BoxLayout(dock, BoxLayout.Y_AXIS));
        dock.setBackground(new Color(25, 25, 25));
        dock.setPreferredSize(new Dimension(DOCK_WIDTH, 100));
        dock.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // load apps once
        List<AppRejistry> apps = ResourceLoader.apps();

        DesktopIcon appsMenu = new DesktopIcon(new AppRejistry(
                "Apps Menu",
                "/image/Logo.svg",
                AppsMenu.class,
                true
        ));
        appsMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        appsMenu.setToolTipText("Apps Menu");
        appsMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        appsMenu.setMaximumSize(new Dimension(60, 60));
        appsMenu.setMinimumSize(new Dimension(40, 40));
        appsMenu.setOpaque(false);

        appsMenu.removeMouseListener(appsMenu.getMouseListeners()[0]);

        // Hover effect: tint background and slightly enlarge
        appsMenu.addMouseListener(new MouseAdapter() {
            private Dimension original = appsMenu.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                appsMenu.setOpaque(true);
                appsMenu.setPreferredSize(new Dimension(Math.min(80, original.width + 12), Math.min(80, original.height + 12)));
                appsMenu.revalidate();
                appsMenu.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                appsMenu.setOpaque(false);
                appsMenu.setPreferredSize(original);
                appsMenu.revalidate();
                appsMenu.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Launch the AppFrame on the EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        appsMenuFrame.setAlwaysOnTop(true);
                        appsMenuFrame.setVisible(!showAppsMenu);

                        showAppsMenu = !showAppsMenu;
                    } catch (Exception ex) {
                        System.out.println("Error");
                    }
                });
            }
        });

        dock.add(appsMenu);
        dock.add(Box.createVerticalStrut(10));

        for (AppRejistry app : apps) {
            if (!app.onToolbar) continue;
            DesktopIcon icon = Utilities.createDockIcon(app);
            dock.add(icon);
            dock.add(Box.createVerticalStrut(10));
        }

        return dock;
    }
    /**
     * Create the layered content pane. We use absolute layout inside the layered pane,
     * but the background image and doors panel will be resized and repositioned on demand.
     */
    private JLayeredPane createContentPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // we'll manage bounds manually
        return layeredPane;
    }

    /**
     * Recompute sizes and rescale the wallpaper to fit the content area.
     */
    private void updateSizesAndBackground() {
        // compute content size: full window size minus dock width
        Dimension full = getSize();
        if (full.width <= 0 || full.height <= 0) {
            // if HomeScreen not yet realized, fall back to screen size
            full = Toolkit.getDefaultToolkit().getScreenSize();
        }
        int contentWidth = Math.max(100, full.width - dockPanel.getPreferredSize().width);
        int contentHeight = Math.max(100, full.height);

        // set content pane bounds and preferred size
        contentPane.setBounds(0, 0, contentWidth, contentHeight);
        contentPane.setPreferredSize(new Dimension(contentWidth, contentHeight));

        // set doors panel to fill content
        doorsPanel.setBounds(0, 0, contentWidth, contentHeight);

        // load and scale background image (safely)
        Image bgImage = null;
        try {
            bgImage = ResourceLoader.image("/image/Wallpapers/Frutiger_Wallpaper.png");
        } catch (Exception e) {
            // Resource missing — keep background blank (or set a solid color)
            contentPane.setBackground(new Color(10, 10, 10));
            return;
        }

        if (bgImage != null) {
            // scale preserving aspect ratio and fill the content area
            Image scaled = getScaledImageToFill(bgImage, contentWidth, contentHeight);
            backgroundLabel.setIcon(new ImageIcon(scaled));
            backgroundLabel.setBounds(0, 0, contentWidth, contentHeight);
            backgroundLabel.revalidate();
            backgroundLabel.repaint();
        }

        // revalidate container so layout and painting update
        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * Scale an image to fill the target area while preserving aspect ratio (center-crop).
     */
    private Image getScaledImageToFill(Image src, int targetW, int targetH) {
        int srcW = src.getWidth(null);
        int srcH = src.getHeight(null);

        if (srcW <= 0 || srcH <= 0) {
            // fallback to basic scaling if we can't determine source size
            return src.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
        }

        double scale = Math.max((double) targetW / srcW, (double) targetH / srcH);
        int newW = (int) Math.round(srcW * scale);
        int newH = (int) Math.round(srcH * scale);

        Image tmp = src.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

        // crop to center if needed
        BufferedImage cropped = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cropped.createGraphics();
        try {
            int x = (newW - targetW) / 2;
            int y = (newH - targetH) / 2;
            g2.drawImage(tmp, -x, -y, null);
        } finally {
            g2.dispose();
        }
        return cropped;
    }
}
