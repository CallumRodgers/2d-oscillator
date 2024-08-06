package mechanics.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ViewPanel extends JPanel {

    private MainPanel mainPanel;

    public ViewPanel() {
        setBackground(new Color(230, 230, 230));
        initLayout();
    }

    private void initLayout() {
        setLayout(new MigLayout());

        JLabel viewLabel = new JLabel("Visualização:");

        CheckBox springCheckBox = new CheckBox("Mostrar Molas");
        springCheckBox.setSelected(true);
        springCheckBox.addActionListener(e -> mainPanel.setDrawSprings(springCheckBox.isSelected()));

        JLabel posLabel = new JLabel("Posição:");
        CheckBox rCB = new CheckBox("r");
        CheckBox rxCB = new CheckBox("<html>r<sub>x</sub></html>");
        CheckBox ryCB = new CheckBox("<html>r<sub>y</sub></html>");

        JLabel velocityLabel = new JLabel("Velocidade:");
        CheckBox vCB = new CheckBox("v");
        CheckBox vxCB = new CheckBox("<html>v<sub>x</sub></html>");
        CheckBox vyCB = new CheckBox("<html>v<sub>y</sub></html>");

        JLabel forceLabel = new JLabel("Força:");
        CheckBox fCB = new CheckBox("F");
        CheckBox fxCB = new CheckBox("<html>F<sub>x</sub></html>");
        CheckBox fyCB = new CheckBox("<html>F<sub>y</sub></html>");

        rCB.addActionListener(e -> mainPanel.setDrawVector(0, rCB.isSelected()));
        rxCB.addActionListener(e -> mainPanel.setDrawVector(1, rxCB.isSelected()));
        ryCB.addActionListener(e -> mainPanel.setDrawVector(2, ryCB.isSelected()));
        vCB.addActionListener(e -> mainPanel.setDrawVector(3, vCB.isSelected()));
        vxCB.addActionListener(e -> mainPanel.setDrawVector(4, vxCB.isSelected()));
        vyCB.addActionListener(e -> mainPanel.setDrawVector(5, vyCB.isSelected()));
        fCB.addActionListener(e -> mainPanel.setDrawVector(6, fCB.isSelected()));
        fxCB.addActionListener(e -> mainPanel.setDrawVector(7, fxCB.isSelected()));
        fyCB.addActionListener(e -> mainPanel.setDrawVector(8, fyCB.isSelected()));

        JLabel sizeLabel = new JLabel("Diâmetro da bola:");
        JSpinner sizeTF = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
        sizeTF.addChangeListener(e -> mainPanel.setObjSize((Integer) sizeTF.getValue()));
        JLabel sizeUL = new JLabel("m");

        add(viewLabel, "cell 0 0, wrap, spanx");
        add(springCheckBox, "cell 0 1, wrap, spanx");

        add(posLabel, "cell 0 2, wrap, spanx");
        add(rCB, "cell 0 3, wrap");
        add(rxCB, "cell 1 3, wrap");
        add(ryCB, "cell 2 3, wrap");

        add(velocityLabel, "cell 0 4, wrap, spanx");
        add(vCB, "cell 0 5, wrap");
        add(vxCB, "cell 1 5, wrap");
        add(vyCB, "cell 2 5, wrap");

        add(forceLabel, "cell 0 6, wrap, spanx");
        add(fCB, "cell 0 7, wrap");
        add(fxCB, "cell 1 7, wrap");
        add(fyCB, "cell 2 7, wrap");

        add(sizeLabel, "cell 0 8, wrap, spanx");
        add(sizeTF, "cell 0 9, span 2 1, wrap");
        add(sizeUL, "cell 2 9, wrap");

        for (Component c : getComponents()) {
            if (c instanceof JLabel l) {
                l.setForeground(Color.BLACK);
            } else if (c instanceof JCheckBox b) {
                b.setForeground(Color.BLACK);
            }
        }
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    private class CheckBox extends JCheckBox {

        public CheckBox(String text) {
            super(text);
            setFocusPainted(false);
            setBackground(ViewPanel.this.getBackground());
            setVerticalTextPosition(SwingConstants.TOP);
        }
    }
}
