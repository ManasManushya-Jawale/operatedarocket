package operatedarocket.apps.FlowerMail;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import operatedarocket.ui.AppFrame;
import operatedarocket.util.Mail.Mail;

import java.awt.*;
import java.awt.event.ActionEvent;

public class FlowerMailApplication extends AppFrame {
    public JPanel mails;
    public JTextArea preview;
    public JSplitPane separator;

    public FlowerMailApplication() {
        super("Flower Mail - Mails are dandelions, flying in the sky");

        preview = new JTextArea();
        preview.setEditable(false);

        mails = new JPanel();
        mails.setLayout(new BoxLayout(mails, BoxLayout.Y_AXIS));

        for (Mail mail : FlowerMailBackend.mails) {
            if (mail.send) {
                System.out.println("YoU'Ve GoT a MaIl!");
                mails.add(new JButton(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        preview.setText(mail.getText());
                    }
                }){{
                    setText(mail.sender + ": " + mail.title);
                }});
            }
        }
        
        separator = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mails, preview);
        separator.setResizeWeight(0.25);

        addContent(separator);
        setBounds(300, 500, 700, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        new FlowerMailApplication();
    }
}
