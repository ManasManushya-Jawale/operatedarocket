package operatedarocket.util.Apps;

import operatedarocket.ui.AppFrame;

public class AppRejistry {
    public String name;
    public String image;
    public Class<? extends AppFrame> mainClass;

    public AppRejistry(String name, String image, Class<? extends AppFrame> mainClass) {
        this.name = name;
        this.image = image;
        this.mainClass = mainClass;
    }
}
