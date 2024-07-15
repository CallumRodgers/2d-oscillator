package mechanics;

import mechanics.ui.Window;
import mechanics.ui.*;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static void startDefault() {
        Window window = new Window("Oscilador Bidimensional", 900, 600);
        JRootPane rootPane = window.getRootPane();
        rootPane.setLayout(new BorderLayout());

        MainPanel mainPanel = new MainPanel();
        SidePanel sidePanel = new SidePanel();
        TimePanel timePanel = new TimePanel();
        ViewPanel viewPanel = new ViewPanel();
        sidePanel.setMainPanel(mainPanel);
        timePanel.setMainPanel(mainPanel);
        viewPanel.setMainPanel(mainPanel);
        mainPanel.setTimePanel(timePanel);

        rootPane.add(sidePanel, BorderLayout.EAST);
        rootPane.add(timePanel, BorderLayout.NORTH);
        rootPane.add(mainPanel, BorderLayout.CENTER);
        rootPane.add(viewPanel, BorderLayout.WEST);
        window.display();

        mainPanel.begin();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                    UnsupportedLookAndFeelException e) {
                System.err.println("Could not set look and feel: " + e);
            }
            startDefault();
        });
    }
}
