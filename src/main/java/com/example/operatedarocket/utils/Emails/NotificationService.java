package com.example.operatedarocket.utils.Emails;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import org.jline.jansi.Ansi;
import org.springframework.stereotype.Component;

import static com.example.operatedarocket.OperateDaRocketApp.util;

@Component
public class NotificationService {

    private static final List<String> notifications = new ArrayList<>();

    public static void notify(String title, String message) {
        notifications.add(message);
        System.out.println(
                Ansi
                        .ansi()
                        .fgBrightYellow()
                        .a("[NOTIFICATION] ")
                        .reset()
                        .fgBrightCyan()
                        .a(title)
                        .a(" - ")
                        .reset()
                        + message);
    }

    public static List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public static void clear() {
        notifications.clear();
    }

    public static void notify(String str) {
        String[] words = str.split(" ");
        StringBuilder displayStr = new StringBuilder();
        for (int i=0;i<str.length();i++) {
            
        }

        JDialog notification = new JDialog();
        notification.setTitle("Inbox Update");
        notification.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        notification.setLocationRelativeTo(null);
        notification.setLayout(new BorderLayout());
        
        JTextArea text = new JTextArea(str);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollBar scroll = new JScrollBar(JScrollBar.VERTICAL);
        scroll.add(text);

        notification.add(scroll, BorderLayout.CENTER);
        notification.setSize(300, 200);
        notification.setVisible(true);
    }

    public static void notifyEmail(String id) {
        Email e = util.eReader.getEmail(id);
        String user = e.id, title = e.title;
        util.eReader.emails.forEach(e1 -> {
            if (e.uuid == e1.uuid) {
                e1.sent = true;
            }
        });
        notify(user + " - " + title);
    }
}
