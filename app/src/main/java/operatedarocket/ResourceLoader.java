package operatedarocket;

import operatedarocket.ui.AppFrame;
import operatedarocket.util.Apps.AppRejistry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {

    public static Image image(String resourcePath) {
        try {
            return ImageIO.read(file(resourcePath));
        } catch (IOException e) {
            System.out.println("File " + resourcePath + " not found");
            return null;
        }
    }

    public static File file(String resourcePath) {
        // Normalize path for logging
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < resourcePath.length(); i++) {
            char ch = resourcePath.charAt(i);
            str.append(ch == '/' ? File.separator : ch);
        }
        System.out.println("Normalized path: " + str);

        // Get resource URL
        URL url = OperateDaRocketApplication.class.getResource(resourcePath);
        if (url == null) {
            System.out.println("Resource not found: " + resourcePath);
            return null;
        }

        // Convert URL to File
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            System.out.println("Failed to convert URL to File: " + e.getMessage());
            return null;
        }
    }

    public static Font font(String resourcePath) {

        InputStream fontIS = OperateDaRocketApplication.class.getResourceAsStream(resourcePath);
        Font customFont;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontIS).deriveFont(12f);
            return customFont;
        } catch (Exception e) {
            System.out.println("Smthing went wrong");
            return null;
        }

    }

    public static List<AppRejistry> apps() throws Exception {
        ArrayList<AppRejistry> apps = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        NodeList nodes = document.getElementsByTagName("apps");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NodeList childNodes = node.getChildNodes();

            String name = childNodes.item(0).getTextContent();
            String image = childNodes.item(1).getTextContent();
            String appClass = childNodes.item(2).getTextContent();

            apps.add(new AppRejistry(name, image, ((Class<? extends AppFrame>)Class.forName(appClass))));
        }
        return apps;
    }
}
