package mechanics.ui;

import mechanics.BidimensionalOscillator;
import mechanics.physics.Time;
import mechanics.physics.vectors.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.*;

import static java.lang.Math.*;

/**
 * Main drawing panel where the simulation occurs.
 */
public class MainPanel extends JPanel {

    private static final Color BG_COLOR = new Color(240, 240, 250);
    private static final Color PATH_COLOR = new Color(100, 100, 100);
    private static final Color AXIS_COLOR = new Color(50, 50, 50);

    private static final Stroke PATH_STROKE = new BasicStroke(1.5f);
    private static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
    private static final Stroke VECTOR_STROKE = new BasicStroke(1.5f);

    private static final double SPRING_GAP = 30;

    private final Ellipse2D object;
    private final Path2D path;
    private final Time time;

    private BidimensionalOscillator oscillator;
    private int objSize = 20;

    private Path2D spring1, spring2, spring3, spring4;
    private AffineTransform at1, at2, at3, at4;

    private TimePanel timePanel;

    private boolean drawSprings = true;
    private final boolean[] drawVectors = new boolean[9];

    public MainPanel() {
        setBackground(Color.WHITE);
        this.oscillator = new BidimensionalOscillator(1, 1, 1);
        oscillator.setR0(new Vector2d(100, 100));
        oscillator.setV0(Vector2d.NULL);
        oscillator.prepare();
        this.object = new Ellipse2D.Double();
        this.path = new Path2D.Double();
        this.time = new Time();
        createSprings();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reset();
            }
        });
    }

    public void setDrawSprings(boolean drawSprings) {
        this.drawSprings = drawSprings;
    }

    private void createSprings() {
        this.spring1 = new Path2D.Double();
        this.spring2 = new Path2D.Double();
        this.spring3 = new Path2D.Double();
        this.spring4 = new Path2D.Double();

        // Moving springs to origin.
        spring1.moveTo(0, 0);
        spring2.moveTo(0, 0);
        spring3.moveTo(0, 0);
        spring4.moveTo(0, 0);

        for (int i = 1; i <= 20; i += 2) {
            double pi2 = Math.PI / 2;
            double y1 = Math.sin(pi2 * i);
            double y2 = Math.sin(pi2 * (i + 1));
            spring1.quadTo(pi2 * i, y1, pi2 * (i + 1), y2);
            spring2.quadTo(pi2 * i, y1, pi2 * (i + 1), y2);
            spring3.quadTo(pi2 * i, y1, pi2 * (i + 1), y2);
            spring4.quadTo(pi2 * i, y1, pi2 * (i + 1), y2);
        }

        at1 = new AffineTransform();
        at2 = new AffineTransform();
        at3 = new AffineTransform();
        at4 = new AffineTransform();
    }

    public void setDrawVector(int index, boolean flag) {
        drawVectors[index] = flag;
    }

    public void setTimePanel(TimePanel timePanel) {
        this.timePanel = timePanel;
    }

    public void setObjSize(int objSize) {
        double change = (double) objSize / this.objSize;
        double x = spring2.getBounds2D().getX();
        double y = spring1.getBounds2D().getY();
        at1.setToScale(1.0, change);
        at2.setToScale(change, 1.0);
        at1.translate(0.0, (y + objSize * 0.75) * (1 / change - 1));
        at2.translate((x + objSize * 0.75) * (1 / change - 1), 0.0);
        spring1.transform(at1);
        spring2.transform(at2);
        spring3.transform(at1);
        spring4.transform(at2);
        at1.setToIdentity();
        this.objSize = objSize;
    }

    public void begin() {
        time.setFrequency(60);
        time.setOnUpdate(() -> SwingUtilities.invokeLater(() -> {
            repaint();
            timePanel.getTimeLabel().setText(String.format("%.3f", time.now()));
        }));
        time.start();
    }

    public Time getTime() {
        return time;
    }

    public void reset() {
        time.reset();
        path.reset();
        createSprings();
        firstDraw = true;
    }

    public void setOscillator(BidimensionalOscillator oscillator) {
        this.oscillator = oscillator;
        reset();
    }

    private boolean firstDraw = true;
    private int w, h;
    private double cx, cy;
    private double lastX, lastY;
    private final StringBuilder xB = new StringBuilder("X: ");
    private final StringBuilder yB = new StringBuilder("Y: ");
    private final StringBuilder vxB = new StringBuilder("Vx: ");
    private final StringBuilder vyB = new StringBuilder("Vy: ");
    private final StringBuilder fxB = new StringBuilder("Fx: ");
    private final StringBuilder fyB = new StringBuilder("Fy: ");

    @Override
    protected void paintComponent(Graphics g) {
        // Preparing graphics object for painting.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        // Clearing out the entire panel with bg colour.
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0, 0, w, h);

        // Drawing axes.
        g2d.setColor(AXIS_COLOR);
        g2d.drawLine(0, 0, w - 1, 0); // Top.
        g2d.drawLine(w - 1, 0, w - 1, h - 1); // Right.
        g2d.drawLine(0, h - 1, w - 1, h - 1); // Bottom.
        g2d.drawLine(0, 0, 0, h - 1); // Left.

        g2d.drawLine(w / 2, 0, w / 2, h);
        g2d.drawLine(0, h / 2, w, h / 2);

        // Computing oscillator variables.
        double t = time.now();
        double[] xVars = oscillator.xVars(t);
        double[] yVars = oscillator.yVars(t);
        double x = xVars[0];
        double y = yVars[0];
        double vx = xVars[1];
        double vy = yVars[1];
        double kineticEnergy = 0.5 * oscillator.getM() * (vx*vx + vy*vy);
        double potentialEnergy = 0.5 * (oscillator.getKx() * x*x + oscillator.getKy() * y*y);
        double totalEnergy = kineticEnergy + potentialEnergy;

        int fontH = g2d.getFontMetrics().getHeight();
        g2d.drawString("Energia Total: " + format(totalEnergy, "J"), 10, fontH);
        g2d.drawString("T + U: " + format(kineticEnergy, "J") + " + " + format(potentialEnergy, "J"), 10, fontH * 2 + 10);

        if (firstDraw) {
            // Computing size variables.

            w = getWidth();
            h = getHeight();
            cx = w / 2.0;
            cy = h / 2.0;
            lastX = 0;
            lastY = 0;

            adjustSprings();

            path.moveTo(cx + x, cy - y);
            firstDraw = false;
        }

        // Drawing our path.
        path.lineTo(cx + x, cy - y);
        g2d.setStroke(PATH_STROKE);
        g2d.setColor(PATH_COLOR);
        g2d.draw(path);

        double dx = x - lastX;
        double dy = y - lastY;

        Rectangle2D spr1Rect = spring1.getBounds2D();
        Rectangle2D spr2Rect = spring2.getBounds2D();
        Rectangle2D spr3Rect = spring3.getBounds2D();
        Rectangle2D spr4Rect = spring4.getBounds2D();

        double s1scale = 1 + (dx / spr1Rect.getWidth());
        double s2scale = 1 - (dy / spr2Rect.getHeight());
        double s3scale = 1 - (dx / spr3Rect.getWidth());
        double s4scale = 1 + (dy / spr4Rect.getHeight());

        at1.setToScale(s1scale, 1.0);
        at2.setToScale(1.0, s2scale);
        at3.setToScale(s3scale, 1.0);
        at4.setToScale(1.0, s4scale);

        double s1X = spr1Rect.getX();
        double s2Y = spr2Rect.getY();
        double s3MX = spr3Rect.getMaxX();
        double s4MY = spr4Rect.getMaxY();
        at1.translate(s1X / s1scale - s1X, -dy);
        at2.translate(dx, s2Y / s2scale - s2Y);
        at3.translate(s3MX / s3scale - s3MX, -dy);
        at4.translate(dx, s4MY / s4scale - s4MY);

        spring1.transform(at1);
        spring2.transform(at2);
        spring3.transform(at3);
        spring4.transform(at4);

        if (drawSprings) {
            g2d.draw(spring1);
            g2d.draw(spring2);
            g2d.draw(spring3);
            g2d.draw(spring4);

            // Drawing spring bases.
            double baseLength = objSize / 2.0;
            int xi = (int) -(x - baseLength), xf = (int) -(x + baseLength);
            int yi = (int) (y - baseLength), yf = (int) (y + baseLength);

            g2d.drawLine((int) s1X, (int) (cy - yi), (int) s1X, (int) (cy - yf));
            g2d.drawLine((int) (cx - xi), (int) s2Y, (int) (cx - xf), (int) s2Y);
            g2d.drawLine((int) s3MX, (int) (cy - yi), (int) s3MX, (int) (cy - yf));
            g2d.drawLine((int) (cx - xi), (int) s4MY, (int) (cx - xf), (int) s4MY);
        }

        // Drawing vectors.
        for (int i = 0; i < drawVectors.length; i++) {
            if (drawVectors[i]) {
                Color color;
                if (i < 3) {
                    color = Color.GREEN;
                } else if (i < 6) {
                    color = Color.CYAN;
                } else {
                    color = Color.RED;
                }
                double x1, y1, x2, y2;
                x1 = y1 = x2 = y2 = 0;
                switch (i) {
                    case 0, 1, 2 -> { // Position vectors.
                        x2 = (i == 2) ? 0 : x;
                        y2 = (i == 1) ? 0 : y;
                    }
                    case 3, 4, 5 -> { // Velocity vectors.
                        x1 = x; y1 = y;
                        x2 = (i == 5) ? x : x + vx;
                        y2 = (i == 4) ? y : y + vy;
                    }
                    case 6, 7, 8 -> { // Force vectors.
                        x1 = x; y1 = y;
                        x2 = (i == 8) ? x : x + xVars[2];
                        y2 = (i == 7) ? y : y + yVars[2];
                    }
                }
                drawVector(g2d, color, x1 + cx, -y1 + cy, cx + x2, cy - y2);
            }
        }

        // Drawing object.
        double halfSize = objSize / 2.0;
        double xOffset = cx - halfSize;
        double yOffset = cy - halfSize;
        g2d.setStroke(DEFAULT_STROKE);
        object.setFrame((xOffset + x), (yOffset - y), objSize, objSize);
        g2d.setColor(Color.BLACK);
        g2d.fill(object);

        // Drawing variables as text.
        xB.setLength(3);
        yB.setLength(3);
        vxB.setLength(4);
        vyB.setLength(4);
        fxB.setLength(4);
        fyB.setLength(4);

        xB.append(format(x, "m"));
        yB.append(format(y, "m"));
        vxB.append(format(xVars[1], "m/s"));
        vyB.append(format(yVars[1], "m/s"));
        fxB.append(format(xVars[2], "N"));
        fyB.append(format(yVars[2], "N"));

        g2d.drawString(xB.toString(), 10, h - 2 * fontH);
        g2d.drawString(yB.toString(), 10, h - fontH);
        g2d.drawString(vxB.toString(), 100, h - 2 * fontH);
        g2d.drawString(vyB.toString(), 100, h - fontH);
        g2d.drawString(fxB.toString(), 200, h - 2 * fontH);
        g2d.drawString(fyB.toString(), 200, h - fontH);

        // Cleaning up.
        g2d.dispose();

        lastX = x;
        lastY = y;
    }

    private void drawVector(Graphics2D g, Color color, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        if (dx == 0 && dy == 0) return;
        g.setColor(color);
        // Drawing vector stem.
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        // Drawing vector head.
        double refX = -10, refY = 0;
        double angle = Math.atan2(dy, dx);
        double leftAngle = angle - Math.PI/4;
        double rightAngle = angle + Math.PI/4;
        double cosL = Math.cos(leftAngle), sinL = Math.sin(leftAngle);
        double cosR = Math.cos(rightAngle), sinR = Math.sin(rightAngle);
        double p1x = refX * cosL - refY * sinL;
        double p1y = refY * cosL + refX * sinL;
        double p2x = refX * cosR - refY * sinR;
        double p2y = refY * cosR + refX * sinR;
        Path2D trianglePath = new Path2D.Double();
        trianglePath.moveTo(x2, y2);
        trianglePath.lineTo(p1x + x2, p1y + y2);
        trianglePath.lineTo(p2x + x2, p2y + y2);
        trianglePath.closePath();
        g.setStroke(VECTOR_STROKE);
        g.fill(trianglePath);
    }

    private String format(double val, String unit) {
        StringBuilder builder = new StringBuilder();
        double log10 = log10(abs(val));
        String num;
        if (log10 > 1) {
            int log = (int) floor(log10);
            num = String.format("%.2f", val / Math.pow(1000, log / 3));
            builder.append(num).append(" ");
            if (log >= 9) {
                builder.append("G");
            } else if (log >= 6) {
                builder.append("M");
            } else if (log >= 3) {
                builder.append("k");
            }
        } else {
            num = String.format("%.2f", round(val * 1000.0) / 1000.0);
            builder.append(num).append(" ");
        }
        builder.append(unit);
        return builder.toString();
    }

    private void adjustSprings() {
        at1.translate(cx, cy);
        at2.translate(cx, cy);
        at3.translate(cx, cy);
        at4.translate(cx, cy);
        at2.rotate(Math.PI / 2);
        at4.rotate(Math.PI / 2);

        double ampX = oscillator.getParamsX()[0];
        double ampY = oscillator.getParamsY()[0];

        double s1w = spring1.getBounds2D().getWidth();
        double s1scale = (ampX + SPRING_GAP) / s1w;
        at1.scale(s1scale, objSize * 0.75);
        at1.translate(-(ampX + SPRING_GAP) / s1scale, 0);

        double s2w = spring2.getBounds2D().getWidth();
        double s2scale = (ampY + SPRING_GAP) / s2w;
        at2.scale(s2scale, objSize * 0.75);
        at2.translate(-(ampY + SPRING_GAP) / s2scale, 0);

        double s3w = spring1.getBounds2D().getWidth();
        double s3scale = (ampX + SPRING_GAP) / s3w;
        at3.scale(s3scale, objSize * 0.75);
        at3.translate((ampX + SPRING_GAP) / s3scale, 0);

        double s4w = spring1.getBounds2D().getWidth();
        double s4scale = (ampY + SPRING_GAP) / s4w;
        at4.scale(s4scale, objSize * 0.75);
        at4.translate((ampY + SPRING_GAP) / s4scale, 0);

        at3.rotate(Math.PI);
        at4.rotate(Math.PI);

        spring1.transform(at1);
        spring2.transform(at2);
        spring3.transform(at3);
        spring4.transform(at4);

        at1.setToIdentity();
        at2.setToIdentity();
        at3.setToIdentity();
        at4.setToIdentity();
    }
}
