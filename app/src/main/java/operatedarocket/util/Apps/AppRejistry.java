package operatedarocket.util.Apps;

import operatedarocket.ui.AppFrame;

import javax.swing.*;

public class AppRejistry {
    public String name;
    public String image;
    public Class<? extends JFrame> mainClass;
    public Boolean onToolbar;

    public AppRejistry(String name, String image, Class<? extends JFrame> mainClass, Boolean onToolbar) {
        this.name = name;
        this.image = image;
        this.mainClass = mainClass;
        this.onToolbar = onToolbar;
    }
}
