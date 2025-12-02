package operatedarocket.apps.AppsMenu;

import operatedarocket.ResourceLoader;
import operatedarocket.Utilities;
import operatedarocket.ui.DesktopIcon;
import operatedarocket.util.Apps.AppRejistry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AppsMenu extends JFrame {
    public JPanel grid;

    public AppsMenu() throws Exception {
        setUndecorated(true);
        setResizable(false);
        setSize(600, 400);
        setLocation(100, 20);

        grid = new JPanel(new GridLayout(4, 6));
        grid.setBackground(new Color(40, 40, 40));

        for (AppRejistry appRejistry : ResourceLoader.apps()) {
            // Since we no longer have an "owner" JFrame, pass `this` instead
            DesktopIcon icon = Utilities.createDockIcon(this, appRejistry);
            grid.add(icon);
        }

        setContentPane(grid);

        // Rounded corners
        setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
