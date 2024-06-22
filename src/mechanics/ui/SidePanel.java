package mechanics.ui;

import mechanics.BidimensionalOscillator;
import mechanics.physics.vectors.Vector2d;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    private static final Color BG = new Color(230, 230, 230);

    private MainPanel mainPanel;

    private PropertyField mTF, kxTF, kyTF, x0TF, y0TF, vx0TF, vy0TF, bTF;
    private JCheckBox dragCB;

    public SidePanel() {
        setBackground(BG);
        initLayout();
        initPropertyFields();
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    private void initLayout() {
        setLayout(new MigLayout(""));

        JLabel propertiesLabel = new JLabel("Propriedades:", JLabel.CENTER);
        JLabel massL = new JLabel("Massa:");
        JLabel kxL = new JLabel("<html>k<sub>x</sub>:</html>");
        JLabel kyL = new JLabel("<html>k<sub>y</sub>:</html>");

        JLabel massUL = new JLabel("kg");
        JLabel kxUL = new JLabel("N/m");
        JLabel kyUL = new JLabel("N/m");

        mTF   = new PropertyField(0.001, 10000.0);
        kxTF  = new PropertyField(0.001, 10000.0);
        kyTF  = new PropertyField(0.001, 10000.0);

        JLabel configL = new JLabel("Configuração inicial:", JLabel.CENTER);
        JLabel x0L = new JLabel("x(0):");
        JLabel y0L = new JLabel("y(0):");
        JLabel vx0L = new JLabel("<html>v<sub>x</sub>(0):</html>");
        JLabel vy0L = new JLabel("<html>v<sub>y</sub>(0):</html>");

        JLabel x0UL = new JLabel("m");
        JLabel y0UL = new JLabel("m");
        JLabel vx0UL = new JLabel("m/s");
        JLabel vy0UL = new JLabel("m/s");

        x0TF  = new PropertyField(-10000.0, 10000.0);
        y0TF  = new PropertyField(-10000.0, 10000.0);
        vx0TF = new PropertyField(-10000.0, 10000.0);
        vy0TF = new PropertyField(-10000.0, 10000.0);

        JLabel freqL = new JLabel("Resolução:");
        JSpinner freqSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 1000, 1));
        freqSpinner.addChangeListener(e -> {
            mainPanel.getTime().setFrequency((Integer) freqSpinner.getValue());
        });
        JLabel freqUL = new JLabel("Hz");
        JLabel freqDL = new JLabel("""
                <html>Uma taxa de atualização maior deixa as curvas mais precisas, mas em compensação
                utiliza mais o processador.</html>
                """);

        dragCB = new JCheckBox("Arrasto Linear");
        dragCB.setBackground(BG);
        dragCB.setFocusPainted(false);
        dragCB.addActionListener(e -> {
            if (dragCB.isSelected()) {
                bTF.setEnabled(true);
            } else {
                bTF.setEnabled(false);
            }
            applyProperties();
        });

        JLabel bL = new JLabel("b:");

        bTF = new PropertyField(0.0, 1000.0);
        bTF.setEnabled(false);

        JLabel bUL = new JLabel("kg/s");

        add(propertiesLabel, "cell 0 0, grow, spanx");
        add(massL, "cell 0 1, grow");
        add(kxL, "cell 0 2, grow");
        add(kyL, "cell 0 3, grow");
        add(mTF, "cell 1 1, grow");
        add(kxTF, "cell 1 2, grow");
        add(kyTF, "cell 1 3, grow");
        add(massUL, "cell 2 1, grow");
        add(kxUL, "cell 2 2, grow");
        add(kyUL, "cell 2 3, grow");

        add(configL, "cell 0 4, grow, spanx");
        add(x0L, "cell 0 5, grow");
        add(y0L, "cell 0 6, grow");
        add(vx0L, "cell 0 7, grow");
        add(vy0L, "cell 0 8, grow");
        add(x0TF, "cell 1 5, grow");
        add(y0TF, "cell 1 6, grow");
        add(vx0TF, "cell 1 7, grow");
        add(vy0TF, "cell 1 8, grow");
        add(x0UL, "cell 2 5, grow");
        add(y0UL, "cell 2 6, grow");
        add(vx0UL, "cell 2 7, grow");
        add(vy0UL, "cell 2 8, grow");

        add(freqL, "cell 0 9, grow, wrap");
        add(freqSpinner, "cell 1 9, grow");
        add(freqUL, "cell 2 9, grow");
        add(freqDL, "cell 0 10, spanx, w 200");

        add(dragCB, "cell 0 11, spanx");
        add(bL, "cell 0 12, grow");
        add(bTF, "cell 1 12, grow");
        add(bUL, "cell 2 12, grow");
    }

    private void initPropertyFields() {
        double[] config = BidimensionalOscillator.DEFAULT_CONFIG;
        mTF.setValue(config[0]);
        kxTF.setValue(config[1]);
        kyTF.setValue(config[2]);
        x0TF.setValue(config[3]);
        y0TF.setValue(config[4]);
        vx0TF.setValue(config[5]);
        vy0TF.setValue(config[6]);
        bTF.setValue(config[7]);
    }

    private void applyProperties() {
        double m = (double) mTF.getValue();
        double kx = (double) kxTF.getValue();
        double ky = (double) kyTF.getValue();
        double x0 = (double) x0TF.getValue();
        double y0 = (double) y0TF.getValue();
        double vx0 = (double) vx0TF.getValue();
        double vy0 = (double) vy0TF.getValue();
        BidimensionalOscillator oscillator = new BidimensionalOscillator(m, kx, ky);
        oscillator.setR0(new Vector2d(x0, y0));
        oscillator.setV0(new Vector2d(vx0, vy0));
        oscillator.setHasDrag(dragCB.isSelected());
        oscillator.setDrag((double) bTF.getValue());
        oscillator.prepare();
        mainPanel.setOscillator(oscillator);
    }

    private class PropertyField extends JSpinner {
        public PropertyField(double min, double max) {
            setModel(new SpinnerNumberModel(1, min, max, 0.001));
            addChangeListener(e -> {
                if (mainPanel != null) {
                    applyProperties();
                }
            });
        }
    }
}
