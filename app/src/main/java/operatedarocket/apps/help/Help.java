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
    public Help() {
        super("Flower Mail - Mails are dandelions, flying in the sky");

        text = new JTextArea("""
        Hi!
        This is Lotus Operating System.
        Made by Manas Pankaj Jawale, this OS is made to keep your life private
        and let you do anything you want, if you have no problem, it's ok
        if you do have a problem, then it's still ok.
        """);
        addContent(text);
        setBounds(300, 500, 700, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Help();
    }
}
