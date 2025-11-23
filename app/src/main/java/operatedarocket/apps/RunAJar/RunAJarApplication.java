package operatedarocket.apps.RunAJar;

import operatedarocket.ui.AppFrame;
import operatedarocket.util.LocalFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class RunAJarApplication extends AppFrame {

    public JLabel title;
    public JButton runAJar;

    public RunAJarApplication() {
        super("Ja Runner");

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        title = new JLabel("Ja Runner");
        title.setFont(LocalFonts.INTER.deriveFont(Font.BOLD, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        addContent(title);

        runAJar = new JButton(new AbstractAction("Run A Jar") {
            @Override
            public void actionPerformed(ActionEvent e) {
                runAJar();
            }
        });
        runAJar.setAlignmentX(Component.CENTER_ALIGNMENT);
        addContent(runAJar);
    }

    public void runAJar() {
        JFileChooser fileChooser = new JFileChooser();
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath();

            try {
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", path);
                Process pbs = pb.start();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
