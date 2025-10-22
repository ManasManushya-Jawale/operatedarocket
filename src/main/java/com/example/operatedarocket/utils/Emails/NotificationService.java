package com.example.operatedarocket.utils.Emails;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.springframework.stereotype.Component;

import static com.example.operatedarocket.OperateDaRocketApp.util;

@Component
public class NotificationService {

    private static final List<String> notifications = new ArrayList<>();

    public static List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public static void clear() {
        notifications.clear();
    }

    static JDialog dialog;
    public static JDialog showNotification(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            dialog = new JDialog(null, "Notification" + title, JDialog.ModalityType.APPLICATION_MODAL);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea(message);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> {dialog.setVisible(false);dialog.dispose();});
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setSize(350, 220);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        return dialog;
    }

    public static void notifyEmail(String id) {
        Email e = util.eReader.getEmail(id);
        String user = e.id, title = e.title;
        util.eReader.emails.forEach(e1 -> {
            if (e.uuid == e1.uuid) {
                e1.sent = true;
            }
        });
        showNotification("Email Recieved", (user + " - " + title));
    }
}
