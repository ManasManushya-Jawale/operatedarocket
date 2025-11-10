package operatedarocket.ui;

import javax.swing.*;

import operatedarocket.ResourceLoader;
import operatedarocket.LotusOS.HomeScreen;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AppFrame extends JFrame {
    public JPanel topBar;

    JButton maximize;
    JButton close;
    JButton minimize;

    public JPanel content;
    public int screenX = 0, screenY = 0, myX = 0, myY = 0;

    public boolean maximized = false, minimized = false;

    public AppFrame(String title) {
        setAlwaysOnTop(true);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(new Color(50, 50, 50));

        close = new JButton("X");
        close.addActionListener(addListener(() -> onClose()));

        minimize = new JButton("_");
        minimize.addActionListener(addListener(() -> onMinimize()));

        maximize = new JButton("□");
        maximize.addActionListener(addListener(() -> onMaximize()));
        maximize.setPreferredSize(new Dimension(30, 30));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ResourceLoader.font("/fonts/JetBrainsMono-Bold.ttf").deriveFont(16f));
        titleLabel.setForeground(Color.white);

        topBar.add(titleLabel);
        topBar.add(minimize);
        topBar.add(maximize);
        topBar.add(close);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = getX();
                myY = getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                setLocation(myX + deltaX, myY + deltaY);
            }

        });

        content = new JPanel(new BorderLayout());

        add(topBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        });

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

        if (maximized) {
            setSize(600, 400);
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            maximize.setText("□");
        } else {
            this.setSize(HomeScreen.cpSize.width - 80, HomeScreen.cpSize.height);
            setShape(new Rectangle(getSize()));
            maximize.setText("⊙");
        }

        maximized = !maximized;
    }

    public Component addContent(Component component) {
        return content.add(component);
    }

    @Override
    public void setSize(int width, int height) {
        // TODO Auto-generated method stub
        super.setSize(width, height);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
    }

    public static void main(String[] args) {
        new AppFrame("null");
    }
}
