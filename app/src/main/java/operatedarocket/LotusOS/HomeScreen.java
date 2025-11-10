package operatedarocket.LotusOS;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import operatedarocket.ResourceLoader;
import operatedarocket.apps.FlowerMail.FlowerMailApplication;
import operatedarocket.apps.FlowerMail.FlowerMailBackend;
import operatedarocket.apps.help.Help;
import operatedarocket.ui.AppFrame;
import operatedarocket.ui.DesktopIcon;

public class HomeScreen extends JPanel {
    public JPanel dockPanel;
    private JLayeredPane contentPane;
    private JPanel doorsPanel;
    public static Dimension cpSize;

    public HomeScreen() {
        try {
            FlowerMailBackend.init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        dockPanel = createDockPanel();

        cpSize = new Dimension(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height - dockPanel.getHeight());

        contentPane = createContentPane();

        add(dockPanel, BorderLayout.WEST);
        add(contentPane, BorderLayout.CENTER);
    }

    private JPanel createDockPanel() {
        JPanel dock = new JPanel();
        dock.setLayout(new BoxLayout(dock, BoxLayout.Y_AXIS));
        dock.setBackground(new Color(25, 25, 25));
        dock.setPreferredSize(new Dimension(80, 100));
        dock.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Add icons
        dock.add(createDockIcon("/image/FlowerMail.svg", "Flower Mail", FlowerMailApplication.class));
        dock.add(Box.createVerticalStrut(10)); // spacing
        dock.add(createDockIcon("/image/Help.svg", "Help", Help.class));

        return dock;
    }

    private DesktopIcon createDockIcon(String resourcePath, String label, Class<? extends AppFrame> appClass) {
        DesktopIcon icon = new DesktopIcon(getClass().getResource(resourcePath).toExternalForm(), label, appClass);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setToolTipText(label);
        icon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        icon.setMaximumSize(new Dimension(60, 60));

        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                icon.setBackground(new Color(40, 40, 40));
                icon.setOpaque(true);
                icon.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                icon.setOpaque(false);
                icon.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AppFrame frame = appClass.getDeclaredConstructor().newInstance();
                    frame.setAlwaysOnTop(true);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(icon, "Failed to launch " + label, "Error",
                            JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                }
            }
        });

        return icon;
    }

    private JLayeredPane createContentPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(cpSize);

        JLabel background = new JLabel(new ImageIcon(ResourceLoader.image("/image/Bg.png")));
        background.setBounds(0, 0, cpSize.width, cpSize.height);
        layeredPane.add(background, Integer.valueOf(0));

        doorsPanel = new JPanel(null);
        doorsPanel.setOpaque(false);
        doorsPanel.setBounds(0, 0, cpSize.width, cpSize.height);
        layeredPane.add(doorsPanel, Integer.valueOf(1));

        return layeredPane;
    }

}
