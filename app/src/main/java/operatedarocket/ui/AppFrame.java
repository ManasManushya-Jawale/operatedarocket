package operatedarocket.ui;

import javax.swing.*;

import operatedarocket.LotusOS.HomeScreen;

import java.awt.*;
import java.awt.event.*;

public class AppFrame extends JFrame {
    public JPanel topBar;
    public JPanel content;

    public AppFrame(String title) {
        setAlwaysOnTop(true);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(new Color(50, 50, 50));

        JButton close = new JButton("X");
        close.addActionListener(addListener(() -> onClose()));

        JButton minimize = new JButton("_");
        minimize.addActionListener(addListener(() -> onMinimize()));

        JButton maximize = new JButton("[]");
        maximize.addActionListener(addListener(() -> onMaximize()));

        JLabel titleLabel = new JLabel(
                String.format("<html><div style='width: 100%;'>%s</div></html>",
                        getTitle()));

        topBar.add(maximize);
        topBar.add(minimize);
        topBar.add(close);

        content = new JPanel();

        add(topBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public AbstractAction addListener(Runnable r) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                r.run();
            }
        };
    }

    public void onClose() {
        dispose();
    }

    public void onMinimize() {
        this.setVisible(false);
    }

    public void onMaximize() {
        System.out.println(HomeScreen.cpSize.toString());
        this.setSize(HomeScreen.cpSize.width - 80, HomeScreen.cpSize.height);
    }

    public Component addContent(Component component) {
        return content.add(component);
    }

    public static void main(String[] args) {
        new AppFrame("null");
    }
}
