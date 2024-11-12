package mechanics;

import mechanics.physics.vectors.Vector2d;

import static java.lang.Math.*;

public class BidimensionalOscillator {

    public static final int A = 0;
    public static final int B = 1;
    public static final int W0 = 2;
    public static final int W1 = 3;
    public static final int BETA = 4;
    public static final int WP = 5;
    public static final int CP = 6;
    public static final int CM = 7;
    public static final double[] CONFIGURATION = {
            1.0,
            1.0,
            1.0,
            100.0,
            100.0,
            0.0,
            0.0,
            0.0,
    };

    private final double m;
    private final double kx, ky;
    private double b;
    private Vector2d r0;
    private Vector2d v0;

    private boolean hasDrag;

    /**
     * Computed oscillator params.
     */
    private final double[] paramsX, paramsY;

    public BidimensionalOscillator(double m, double kx, double ky) {
        this.m = m;
        this.kx = kx;
        this.ky = ky;
        this.paramsX = new double[8];
        this.paramsY = new double[8];
    }

    public void setR0(Vector2d r0) {
        this.r0 = r0;
    }

    public void setV0(Vector2d v0) {
        this.v0 = v0;
    }

    public void setDrag(double b) {
        this.b = max(b, 0.0);
    }

    public void setHasDrag(boolean hasDrag) {
        this.hasDrag = hasDrag;
    }

    public void prepare() {
        // Computing natural frequency w0.
        paramsX[W0] = sqrt(kx / m);
        paramsY[W0] = sqrt(ky / m);

        // Computing coefficient A.
        paramsX[A] = v0.v1 / paramsX[W0];
        paramsY[A] = v0.v2 / paramsY[W0];

        // Computing coefficient B.
        paramsX[B] = r0.v1;
        paramsY[B] = r0.v2;

        // Computing drag constants.
        if (hasDrag && b > 0) {
            // Computing beta.
            paramsX[BETA] = b / (2 * m);
            paramsY[BETA] = b / (2 * m);

            paramsX[W1] = sqrt(-paramsX[BETA]*paramsX[BETA] + (kx / m));
            paramsY[W1] = sqrt(-paramsY[BETA]*paramsY[BETA] + (ky / m));

            // Computing supercritical case constants.
            paramsX[WP] = sqrt(paramsX[BETA]*paramsX[BETA] - (kx / m));
            paramsY[WP] = sqrt(paramsY[BETA]*paramsY[BETA] - (ky / m));

            paramsX[CM] = (v0.v1 + r0.v1 * (-paramsX[BETA] + paramsX[WP])) / (-2 * paramsX[WP]);
            paramsX[CP] = r0.v1 - paramsX[CM];

            paramsY[CM] = (v0.v2 + r0.v2 * (-paramsY[BETA] + paramsY[WP])) / (-2 * paramsY[WP]);
            paramsY[CP] = r0.v2 - paramsY[CM];
        }

        System.out.println("---------------------------------");
        System.out.println("X Parameters:\n" + paramsToString(paramsX) + "\n\nY Parameters: " + paramsToString(paramsY));
        System.out.println("---------------------------------");
    }

    public double[] xVars(double t) {
        double x, vx, fx;
        if (!hasDrag || b == 0) {
            double trigArg = paramsX[W0] * t;
            double sin = sin(trigArg), cos = cos(trigArg);
            x = paramsX[A] * sin + paramsX[B] * cos;
            vx = paramsX[W0] * (paramsX[A] * cos - paramsX[B] * sin);
            fx = -kx * x;
        } else {
            if (paramsX[BETA] < paramsX[W0]) {
                double trigArg = paramsX[W1] * t;
                double sin = sin(trigArg), cos = cos(trigArg);
                x = exp(-paramsX[BETA] * t) * (paramsX[A] * sin + paramsX[B] * cos);
                vx = -paramsX[BETA] * x + exp(-paramsX[BETA] * t) * (paramsX[A] * cos - paramsX[B] * sin);
            } else if (paramsX[BETA] == paramsX[W0]) {
                double c1pc2t = (r0.v1 + (v0.v1 + paramsX[BETA] * r0.v1) * t);
                x = exp(-paramsX[BETA] * t) * c1pc2t;
                vx = exp(-paramsX[BETA] * t) * (-paramsX[BETA] * c1pc2t + v0.v1 + paramsX[BETA] * r0.v1);
            } else {
                double rp = -paramsX[BETA] + paramsX[WP];
                double rm = -paramsX[BETA] - paramsX[WP];
                double cpExp = paramsX[CP] * exp(rp * t);
                double cmExp = paramsX[CM] * exp(rm * t);
                x  = cpExp      + cmExp;
                vx = cpExp * rp + cmExp * rm;
            }
            fx = -kx * x - b * vx;
        }
        return new double[] {
                x, vx, fx
        };
    }

    public double[] yVars(double t) {
        double y, vy, fy;
        if (!hasDrag || b == 0) {
            double trigArg = paramsY[W0] * t;
            double sin = sin(trigArg), cos = cos(trigArg);
            y = paramsY[A] * sin + paramsY[B] * cos;
            vy = paramsY[W0] * (paramsY[A] * cos - paramsY[B] * sin);
            fy = -kx * y;
        } else {
            if (paramsY[BETA] < paramsY[W0]) {
                double trigArg = paramsY[W1] * t;
                double sin = sin(trigArg), cos = cos(trigArg);
                y = exp(-paramsY[BETA] * t) * (paramsY[A] * sin + paramsY[B] * cos);
                vy = -paramsY[BETA] * y + exp(-paramsY[BETA] * t) * (paramsY[A] * cos - paramsY[B] * sin);
            } else if (paramsY[BETA] == paramsY[W0]) {
                double c1pc2t = (r0.v1 + (v0.v1 + paramsY[BETA] * r0.v1) * t);
                y = exp(-paramsY[BETA] * t) * c1pc2t;
                vy = exp(-paramsY[BETA] * t) * (-paramsY[BETA] * c1pc2t + v0.v1 + paramsY[BETA] * r0.v1);
            } else {
                double rp = -paramsY[BETA] + paramsY[WP];
                double rm = -paramsY[BETA] - paramsY[WP];
                double cpExp = paramsY[CP] * exp(rp * t);
                double cmExp = paramsY[CM] * exp(rm * t);
                y  = cpExp      + cmExp;
                vy = cpExp * rp + cmExp * rm;
            }
            fy = -kx * y - b * vy;
        }
        return new double[] {
                y, vy, fy
        };
    }

    public double[] getParamsX() {
        return paramsX;
    }

    public double[] getParamsY() {
        return paramsY;
    }

    public double getM() {
        return m;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    private String paramsToString(double[] params) {
        return "A (const): " + params[A] + "\n" +
                "B (const.): " + params[B] + "\n" +
                "W0: " + params[W0] + "\n" +
                "Beta = b/2m: " + params[BETA] + "\n" +
                "W1 = sqrt(W0² - Beta²): " + params[W1] + "\n" +
                "W': " + params[WP] + "\n" +
                "C+ (const): " + params[CP] + "\n" +
                "C- (const): " + params[CM];
    }

}
