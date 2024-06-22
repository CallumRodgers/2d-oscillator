package mechanics;

import mechanics.physics.vectors.Vector2d;

import java.util.Arrays;

import static java.lang.Math.*;

public class BidimensionalOscillator {

    public static final double[] DEFAULT_CONFIG = {
            1.0, // m
            1.0, // kx
            1.0, // ky
            100.0, // x0
            100.0, // y0
            0.0, // vx0
            0.0, // vy0
            0.0, // b
    };

    public static final int A = 0;
    public static final int W0 = 1;
    public static final int DEL = 2;
    public static final int W1 = 3;
    public static final int BETA = 4;
    public static final int WP = 5;
    public static final int CP = 6;
    public static final int CM = 7;

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

        // Computing amplitude A: A² = x² + (vx / w0)²
        paramsX[A] = hypot(r0.v1, (v0.v1 / paramsX[W0]));
        paramsY[A] = hypot(r0.v2, (v0.v2 / paramsY[W0]));

        if (paramsX[A] == 0) {
            paramsX[DEL] = 0;
        } else if (v0.v1 == 0) {
            paramsX[DEL] = acos(r0.v1 / paramsX[A]);
        } else {
            paramsX[DEL] = atan(v0.v1 / (r0.v1 * paramsX[W0]));
        }

        if (paramsY[A] == 0) {
            paramsY[DEL] = 0;
        } else if (v0.v2 == 0) {
            paramsY[DEL] = acos(r0.v2 / paramsY[A]);
        } else {
            paramsY[DEL] = atan(v0.v2 / (r0.v2 * paramsY[W0]));
        }

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

        System.out.println(Arrays.toString(paramsX) + " " + Arrays.toString(paramsY));
    }

    public double[] xVars(double t) {
        double x, vx, fx;
        if (!hasDrag || b == 0) {
            double trigArg = paramsX[W0] * t - paramsX[DEL];
            x = paramsX[A] * cos(trigArg);
            vx = -paramsX[A] * paramsX[W0] * sin(trigArg);
            fx = -kx * x;
        } else {
            if (paramsX[BETA] < paramsX[W0]) {
                double trigArg = paramsX[W1] * t - paramsX[DEL];
                x = paramsX[A] * exp(-paramsX[BETA] * t) * cos(trigArg);
                vx = paramsX[A] * exp(-paramsX[BETA] * t) * (paramsX[BETA] * cos(trigArg) + paramsX[W1] * sin(trigArg));
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
            double trigArg = paramsY[W0] * t - paramsY[DEL];
            y = paramsY[A] * cos(trigArg);
            vy = -paramsY[A] * paramsY[W0] * sin(trigArg);
            fy = -kx * y;
        } else {
            if (paramsY[BETA] < paramsY[W0]) {
                double trigArg = paramsY[W1] * t - paramsY[DEL];
                y = paramsY[A] * exp(-paramsY[BETA] * t) * cos(trigArg);
                vy = paramsY[A] * exp(-paramsY[BETA] * t) * (paramsY[BETA] * cos(trigArg) + paramsY[W1] * sin(trigArg));
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

    public double getB() {
        return b;
    }
}
