package com.example.operatedarocket.utils.window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class WebImageFrame extends JFrame {
    private BufferedImage image;

    public WebImageFrame(String title, String resourcePath) {
        super(title);
        try {
            URL imageUrl = getClass().getResource(resourcePath);
            if (imageUrl != null) {
                image = javax.imageio.ImageIO.read(imageUrl);
            } else {
                System.err.println("Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Add a custom panel to draw the image
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });

        setVisible(true);
    }
}
