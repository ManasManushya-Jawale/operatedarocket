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
            DesktopIcon icon = Utilities.createDockIcon(appRejistry);
            grid.add(icon);
        }

        setContentPane(grid);

        setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

        setVisible(true);

    }
}
