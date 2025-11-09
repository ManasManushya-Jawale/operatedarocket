package operatedarocket;

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ComponentView {
    public static void main(String[] args) {
        JFrame componentViewFrame = new JFrame("Component View");
        componentViewFrame.add(new JButton(){{
            setSize(125, 100);
        }});
        componentViewFrame.setLocationRelativeTo(null);
        componentViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        componentViewFrame.pack();
        componentViewFrame.setVisible(true);
    }
}
