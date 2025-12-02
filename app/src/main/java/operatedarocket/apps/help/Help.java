package operatedarocket.apps.help;

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

public class Help extends AppFrame {
    public JTextArea text;
    public Help(String title) {
        super(title);

        text = new JTextArea("""
            Theres no help
        """);
        text.setFont(text.getFont().deriveFont(18f));
        text.setEditable(false);
        addContent(text);
        setBounds(300, 500, 700, 500);
        setVisible(true);
    }

}
