package operatedarocket.ui;

import java.awt.Color;

import org.apache.batik.swing.JSVGCanvas;

public class DesktopIcon extends JSVGCanvas {
    public String name;
    public Class<? extends AppFrame> app;

    public DesktopIcon(String uri, String name, Class<? extends AppFrame> app) {
        this.name = name;
        this.app = app;
        setToolTipText(name);

        setURI(uri);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
}
