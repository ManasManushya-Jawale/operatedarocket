package operatedarocket.apps.FlowerMail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import operatedarocket.ui.AppFrame;
import operatedarocket.util.Mail.Mail;

public class FlowerMailApplication extends AppFrame {

    private JPanel mailsPanel;
    private JTextArea previewArea;
    private JSplitPane splitPane;

    public FlowerMailApplication() {
        super("Flower Mail — Mails are dandelions, flying in the sky");

        SwingUtilities.invokeLater(() -> {
            initUI();
            loadMails();
            finalizeUI();
        });
    }

    /**
     * Initialize the main UI components before loading content.
     */
    private void initUI() {
        // Preview area (right side)
        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);

        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(BorderFactory.createEmptyBorder());

        // Mail list panel (left side)
        mailsPanel = new JPanel();
        mailsPanel.setLayout(new BoxLayout(mailsPanel, BoxLayout.Y_AXIS));

        JScrollPane mailScroll = new JScrollPane(mailsPanel);
        mailScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // SplitPane (mail list | preview)
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mailScroll, previewScroll);
        splitPane.setResizeWeight(0.30);
        splitPane.setContinuousLayout(true);

        addContent(splitPane);
    }

    /**
     * Load mails from the backend and create buttons for them.
     */
    private void loadMails() {
        for (Mail mail : FlowerMailBackend.mails) {
            if (mail == null) continue;
            if (!mail.send) continue;

            String sender = mail.sender != null ? mail.sender : "Unknown";
            String title = mail.title != null ? mail.title : "(No Title)";
            String text  = mail.getText() != null ? mail.getText() : "(No Content)";

            JButton mailButton = new JButton(new AbstractAction(sender + ": " + title) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    previewArea.setText(text);
                }
            });

            mailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            mailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            mailsPanel.add(mailButton);
            mailsPanel.add(Box.createVerticalStrut(5));

            System.out.println("You’ve got a mail!");
        }
    }

    /**
     * Final window setup after all UI content is loaded.
     */
    private void finalizeUI() {
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // After window is visible, adjust the divider for correct proportion
        SwingUtilities.invokeLater(() ->
                splitPane.setDividerLocation(0.30)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlowerMailApplication::new);
    }
}
