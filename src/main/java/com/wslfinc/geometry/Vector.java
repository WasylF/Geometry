package com.wslfinc.geometry;

import static java.lang.Math.*;
import static com.wslfinc.geometry.Constants.*;
import static com.wslfinc.geometry.Math.*;

/**
 *
 * @author Wsl_F
 */
public class Vector extends Point {

    public Vector() {
        super();
    }

    public Vector(double x, double y) {
        super(x, y);
    }

    public Vector(Vector v) {
        super(v);
    }

    public Vector(Point begin, Point end) {
        init(begin, end);
    }

    public final void init(Point begin, Point end) {
        if (begin.manhattanDistance(end) < EPS) {
            throw new IllegalArgumentException("Vector: Points the same. Vector couldn't be created");
        }
        this.x = end.x - begin.x;
        this.y = end.y - begin.y;
    }

    /**
     * creates vector. begin - {@code segment.a}, end - {@code segment.b}
     *
     * @param segment segment
     */
    public Vector(Segment segment) {
        this(segment.a, segment.b);
    }

    /**
     *
     * @param angle angle with the abscissa
     */
    public Vector(double angle) {
        super();
        Vector v = getVectorByAngle(angle);
        this.x = v.x;
        this.y = v.y;
    }

    @Override
    public String toString() {
        return " ( " + x + " , " + y + " ) : |" + length() + "|";
    }

    /**
     * check equality
     *
     * @param obj object
     * @return true if vectors the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return equals((Vector) obj);
    }

    /**
     * Two vectors are equals if they have the same length and direction
     *
     * @param v second vector
     * @return
     */
    public boolean equals(Vector v) {
        return ((Point) this).equals((Point) v);
    }

    /**
     * check does this vector collinear to v
     *
     * @param v second vector
     * @return true if collinear
     */
    public boolean isCollinear(Vector v) {
        return com.wslfinc.geometry.Math.isDeterminantZero(this, v);
    }

    /**
     *
     * @return length of vector
     */
    public double length() {
        return com.wslfinc.geometry.Math.hypot(x, y);
    }

    /**
     * makes length of vector equals to 1
     *
     * @return is it successful
     */
    public boolean normalize() {
        double length = length();
        if (length > EPS) {
            x /= length;
            y /= length;
            return true;
        }
        return false;
    }

    /**
     * multiplication of a vector by a scalar
     *
     * @param k scalar
     */
    public void multiplyScalar(double k) {
        x *= k;
        y *= k;
    }

    /**
     * Dot product of vectors
     *
     * @param v second vector
     * @return multiplication
     */
    public double multiplyScalar(Vector v) {
        return x * v.x + y * v.y;
    }

    /**
     * Cross product of vectors
     *
     * @param v second vector
     * @return
     */
    public double multiplyVectors(Vector v) {
        return x * v.y - y * v.x;
    }

    /**
     * Calculate signum of cross product of vectors
     *
     * @param v second vector
     * @return int value of signum
     */
    public int sgnMultiplyVectors(Vector v) {
        double mult = multiplyVectors(v);
        return com.wslfinc.geometry.Math.sgn(mult);
    }

    /**
     * rotate this vector by clockwise on angle phi
     *
     * @param phi angle
     */
    public void rotateVector(double phi) {
        double xNew = x * cos(phi) - y * sin(phi);
        double yNew = x * sin(phi) + y * cos(phi);
        this.x = xNew;
        this.y = yNew;
    }

    /**
     *
     * @param angle angle between vector and Ox
     * @return new vector
     */
    public static Vector getVectorByAngle(double angle) {
        if (abs(angle - PI / 2) < EPS_ANGLE) {
            return new Vector(0, 1);
        }
        if (abs(angle + PI / 2) < EPS_ANGLE) {
            return new Vector(0, -1);
        }

        {
            int t = (int) (angle / PI) + 1;
            angle -= (t / 2) * PI * 2;
        }
        // angle [-PI; PI]
        if (abs(angle) < PI / 2) {
            return new Vector(1, tan(angle));
        } else {
            return new Vector(-1, -tan(angle));
        }
    }

    /**
     * Calculation of positive angle between vectors
     *
     * @param v second vector
     * @return angle in radians, if incalculable then -1
     */
    public double getPositiveAngle(Vector v) {
        if (length() < EPS || v.length() < EPS) {
            return -1;
        }
        return abs(getAngle(v));
    }

    /**
     * Calculation of oriented angle between the vectors.
     * <br>positive direction - angle by counterclockwise, negative - clockwise.
     * <br>if (vectors is collinear)
     * <br>{
     * <br> if (sameDirection) returns 0;
     * <br> else // opposite direction
     * <br> returns PI;
     * <br>}
     *
     * @param v second vector
     * @return oriented angle between the vectors in range [-PI,+PI]
     */
    public double getAngle(Vector v) {
        double atan2Y = x * v.y - v.x * y;
        double atan2X = x * v.x + y * v.y;

        if (abs(atan2X) < EPS && abs(atan2Y) < EPS) {
            return 0;
        }

        if (abs(atan2Y) < EPS) {
            atan2Y = EPS / 2;
        }
        if (abs(atan2X) < EPS) {
            atan2X = 0;
        }

        return atan2(atan2Y, atan2X);
    }

    /**
     * Calculation of oriented angle between the vectors by counterclockwise.
     *
     * <br> if (vectors are uniderctional) returns 0; (!NOT 2*PI)
     * <br> For more details see {@code getAngle)
     *
     * @param v second vector
     * @return angle in range [0;2*PI)
     */
    public double getAngle2PI(Vector v) {
        double angle = getAngle(v);
        if (angle < 0) {
            angle += 2 * PI;
        }
        return angle;
    }

    /**
     * do vectors have the same direction?
     *
     * @param v
     * @return true if angle close to zero
     */
    public boolean isUnidirectional(Vector v) {
        return sgn(x) == sgn(v.x) && sgn(y) == sgn(v.y) && isCollinear(v);
    }

    /**
     * do vectors have the opposit direction?
     *
     * @param v
     * @return true if angle close to PI
     */
    public boolean isOpposite(Vector v) {
        return (sgn(x) != sgn(v.x) || sgn(y) != sgn(v.y)) && isCollinear(v);
    }

    /**
     *
     * @return angle with the abscissa
     */
    public double getAngleToOX() {
        return getAngle(new Vector(1, 0));
    }

    @Override
    public int hashCode() {
        long hash = super.hashCode();
        return (int) (hash * BIG_PRIME % Integer.MAX_VALUE);
    }

}
