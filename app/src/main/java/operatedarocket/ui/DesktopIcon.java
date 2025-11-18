package operatedarocket.ui;

import java.awt.Color;

import operatedarocket.util.Apps.AppRejistry;
import org.apache.batik.swing.JSVGCanvas;

public class DesktopIcon extends JSVGCanvas {
    public String name;
    public Class<? extends AppFrame> app;

    public DesktopIcon(AppRejistry rejistry) {
        this.name = rejistry.name;
        this.app = rejistry.mainClass;
        setToolTipText(name);

        setURI(getClass().getResource(rejistry.image).toExternalForm());
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
}
