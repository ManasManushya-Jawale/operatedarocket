package operatedarocket;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.net.URL;

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
}
