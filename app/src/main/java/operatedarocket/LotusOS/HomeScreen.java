package operatedarocket.LotusOS;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import operatedarocket.ResourceLoader;
import operatedarocket.apps.AppsMenu.AppsMenu;
import operatedarocket.apps.FlowerMail.FlowerMailBackend;
import operatedarocket.ui.DesktopIcon;
import operatedarocket.util.Apps.AppRejistry;
import operatedarocket.Utilities;
import operatedarocket.util.LocalFonts;
import operatedarocket.util.PlayerData.PlayerData;
import operatedarocket.util.PlayerData.PlayerDataSaverAndReader;

/**
 * Improved HomeScreen:
 * - Proper content sizing (accounts for dock width)
 * - Scaled wallpaper that updates on resize
 * - Single method to create dock icons (no duplicate creation)
 * - App frames launched on EDT and not AlwaysOnTop by default
 * - Small hover effect on icons
 * - Added live clock to dock
 */
public class HomeScreen extends JPanel {
    private static final int DOCK_WIDTH = 80;
    public final JPanel dockPanel;
    private final JLayeredPane contentPane;
    private final JPanel doorsPanel;
    public final JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final JLabel backgroundLabel;
    boolean showAppsMenu = false;

    private final AppsMenu appsMenuFrame;

    public HomeScreen(JFrame owner) throws Exception {
        super(new BorderLayout());
        FlowerMailBackend.init();

        appsMenuFrame = new AppsMenu();

        // Create UI parts
        dockPanel = createDockPanel(owner);
        contentPane = createContentPane();
        doorsPanel = new JPanel(null);
        doorsPanel.setOpaque(false);
        contentPane.add(doorsPanel, Integer.valueOf(1));

        // background label stored so we can update its icon on resize
        backgroundLabel = new JLabel();
        contentPane.add(backgroundLabel, Integer.valueOf(0));

        topBar.setBackground(Color.BLACK);

        // Add clock at bottom
        JLabel clockLabel = new JLabel();
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setFont(LocalFonts.INTER.deriveFont(14f));

        JLabel date = new JLabel();
        date.setForeground(Color.WHITE);
        date.setFont(LocalFonts.INTER.deriveFont(14f));

        Timer timer = new Timer(1000, e -> {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            clockLabel.setText(time);

            LocalDate gameDate = PlayerDataSaverAndReader.load().date;
            String dateText = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(gameDate);
            date.setText(dateText);
        });
        timer.start();

        topBar.add(clockLabel);
        topBar.add(date);

        add(topBar, BorderLayout.NORTH);

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
    private JPanel createDockPanel(JFrame owner) throws Exception {
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

        // Defensive: only remove listener if present
        MouseListener[] listeners = appsMenu.getMouseListeners();
        if (listeners.length > 0) {
            appsMenu.removeMouseListener(listeners[0]);
        }

        // Hover effect: tint background and slightly enlarge
        appsMenu.addMouseListener(new MouseAdapter() {
            private Dimension original = appsMenu.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                appsMenu.setOpaque(true);
                appsMenu.setPreferredSize(new Dimension(
                        Math.min(80, original.width + 12),
                        Math.min(80, original.height + 12)));
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
                SwingUtilities.invokeLater(() -> {
                    try {
                        appsMenuFrame.setAlwaysOnTop(true);
                        appsMenuFrame.setVisible(!showAppsMenu);
                        showAppsMenu = !showAppsMenu;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });

        dock.add(appsMenu);
        dock.add(Box.createVerticalStrut(10));

        // Add other app icons
        for (AppRejistry app : apps) {
            if (!app.onToolbar) continue;
            DesktopIcon icon = Utilities.createDockIcon(owner, app);
            dock.add(icon);
            dock.add(Box.createVerticalStrut(10));
        }

        return dock;
    }

    private JLayeredPane createContentPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        return layeredPane;
    }

    private void updateSizesAndBackground() {
        Dimension full = getSize();
        if (full.width <= 0 || full.height <= 0) {
            full = Toolkit.getDefaultToolkit().getScreenSize();
        }
        int contentWidth = Math.max(100, full.width - dockPanel.getPreferredSize().width);
        int contentHeight = Math.max(100, full.height);

        contentPane.setBounds(0, 0, contentWidth, contentHeight);
        contentPane.setPreferredSize(new Dimension(contentWidth, contentHeight));
        doorsPanel.setBounds(0, 0, contentWidth, contentHeight);

        Image bgImage = null;
        try {
            bgImage = ResourceLoader.image("/image/Wallpapers/Frutiger_Wallpaper.png");
        } catch (Exception e) {
            contentPane.setBackground(new Color(10, 10, 10));
            return;
        }

        if (bgImage != null) {
            Image scaled = getScaledImageToFill(bgImage, contentWidth, contentHeight);
            backgroundLabel.setIcon(new ImageIcon(scaled));
            backgroundLabel.setBounds(0, 0, contentWidth, contentHeight);
            backgroundLabel.revalidate();
            backgroundLabel.repaint();
        }

        contentPane.revalidate();
        contentPane.repaint();
    }

    private Image getScaledImageToFill(Image src, int targetW, int targetH) {
        int srcW = src.getWidth(null);
        int srcH = src.getHeight(null);

        if (srcW <= 0 || srcH <= 0) {
            return src.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
        }

        double scale = Math.max((double) targetW / srcW, (double) targetH / srcH);
        int newW = (int) Math.round(srcW * scale);
        int newH = (int) Math.round(srcH * scale);

        Image tmp = src.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

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
