package operatedarocket;

import operatedarocket.apps.AppsMenu.AppsMenu;
import operatedarocket.ui.AppFrame;
import operatedarocket.ui.DesktopIcon;
import operatedarocket.util.Apps.AppRejistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class Utilities {


    /**
     * Create a DesktopIcon wired with a mouse listener to launch the app via reflection.
     * Includes a small hover effect (icon background + scale-increase).
     */
    public static DesktopIcon createDockIcon(AppRejistry rejistry) {
        DesktopIcon icon = new DesktopIcon(rejistry);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setToolTipText(rejistry.name);
        icon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        icon.setMaximumSize(new Dimension(60, 60));
        icon.setMinimumSize(new Dimension(40, 40));
        icon.setOpaque(false);

        // Hover effect: tint background and slightly enlarge
        icon.addMouseListener(new MouseAdapter() {
            private Dimension original = icon.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                icon.setOpaque(true);
                icon.setPreferredSize(new Dimension(Math.min(80, original.width + 12), Math.min(80, original.height + 12)));
                icon.revalidate();
                icon.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                icon.setOpaque(false);
                icon.setPreferredSize(original);
                icon.revalidate();
                icon.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Launch the AppFrame on the EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        JFrame frame = rejistry.mainClass.getDeclaredConstructor().newInstance();
                        if (rejistry.mainClass != AppsMenu.class) {
                            frame.setLocationRelativeTo(null);
                        }
                        frame.setVisible(true);
                    } catch (InvocationTargetException ite) {
                        showLaunchError(icon, rejistry.name, ite.getTargetException());
                    } catch (Exception ex) {
                        showLaunchError(icon, rejistry.name, ex);
                    }
                });
            }
        });

        return icon;
    }

    public static void showLaunchError(Component parent, String appName, Throwable err) {
        err.printStackTrace();
        JOptionPane.showMessageDialog(parent,
                "Failed to launch " + appName + ":\n" + err.getClass().getSimpleName() + ": " + err.getMessage(),
                "Launch Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
