package mechanics.ui;

import javax.swing.*;

public class Window extends JFrame {

    public Window(String title, int width, int height) {
        this.setTitle(title);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void display() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
