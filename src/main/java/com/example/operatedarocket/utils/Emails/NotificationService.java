package com.example.operatedarocket.utils.Emails;

import java.util.ArrayList;
import java.util.List;

import org.jline.jansi.Ansi;
import org.springframework.stereotype.Component;

import static com.example.operatedarocket.OperateDaRocketApp.util;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

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
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png"); // optional icon
                TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Notification from Java");
                tray.add(trayIcon);
                trayIcon.displayMessage("Inbox Update", str + "(copied to clipboard)", MessageType.INFO);

                System.out.println("CLICKED");
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(str), null);
            } else {
                System.err.println("System tray not supported!");
            }
        } catch (Exception e) {

        }
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
