package mechanics.ui;

import mechanics.physics.Time;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TimePanel extends JPanel {

    private MainPanel mainPanel;
    private PropertyField timeTF;
    private JLabel timeLabel;

    private AboutDialog dialog;

    public TimePanel() {
        setBackground(new Color(240, 240, 240));
        initLayout();
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    private void initLayout() {
        setLayout(new MigLayout("", "[][70!][][][][][][grow][][]", ""));

        JLabel timeL = new JLabel("Tempo (t):");
        timeTF = new PropertyField(0, Double.POSITIVE_INFINITY);
        timeLabel = new JLabel();
        JLabel timeUL = new JLabel("s");

        JButton resetButton = new JButton("Resetar");
        JButton pauseButton = new JButton("Pausar");

        resetButton.addActionListener(e -> mainPanel.reset());
        resetButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> {
            Time t = mainPanel.getTime();
            if (t.isRunning()) {
                pauseButton.setText("Resumir");
                t.pause();
            } else {
                pauseButton.setText("Pausar");
                t.resume();
            }
        });
        pauseButton.setFocusPainted(false);

        JLabel speedL = new JLabel("Velocidade:");
        JSpinner speedTF = new JSpinner(new SpinnerNumberModel(1.0, 0.001, 1000.0, 0.1));
        speedTF.addChangeListener(e -> mainPanel.getTime().setSpeed((Double) speedTF.getValue()));
        JLabel speedUL = new JLabel("x");

        JButton githubButton = new JButton("GitHub");
        githubButton.addActionListener(l -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/CallumRodgers/2d-oscillator"));
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Unable to open link: " + e);
                }
            }
        });

        JButton aboutButton = new JButton("Como Usar");
        aboutButton.addActionListener(e -> {
            if (dialog != null) return;
            dialog = new AboutDialog((JFrame) TimePanel.this.getParent().getParent());
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDeactivated(WindowEvent e) {
                    dialog = null;
                }
            });
        });

        githubButton.setFocusPainted(false);
        aboutButton.setFocusPainted(false);

        add(timeL, "cell 0 0");
        add(timeLabel, "cell 1 0");
        add(timeUL, "cell 2 0");
        add(resetButton, "cell 3 0");
        add(pauseButton, "cell 4 0");

        add(speedL, "cell 5 0");
        add(speedTF, "cell 6 0");
        add(speedUL, "cell 7 0");

        add(githubButton, "cell 8 0");
        add(aboutButton, "cell 9 0");

        for (Component c : getComponents()) {
            if (c instanceof JLabel l) {
                l.setForeground(Color.BLACK);
            }
        }
    }

    private void applyProperties() {
        mainPanel.getTime().setTime((Double) timeTF.getValue());
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public class PropertyField extends JSpinner {
        public PropertyField(double min, double max) {
            setModel(new SpinnerNumberModel(1, min, max, 0.01));
            addChangeListener(e -> {
                if (mainPanel != null) {
                    applyProperties();
                }
            });
        }
    }
}
