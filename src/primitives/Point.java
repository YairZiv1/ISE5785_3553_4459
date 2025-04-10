package primitives;

/**
 * Class Point is the basic class representing a point of Euclidean geometry in  Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class Point {
    /**
     * Represents the 3D coordinates of the point in Cartesian space.
     */
    protected final Double3 coords;

    /**
     * Zero triad (0,0,0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a Point object with the specified x, y, and z coordinates.
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @param z the z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this.coords = new Double3(x, y, z);
    }

    /**
     * Constructs a Point object with the specified coordinates.
     * @param d3 the Double3 object representing the 3D coordinates of the point.
     */
    public Point(Double3 d3) {
        this.coords = d3;
    }

    /**
     * Subtract between a point to this point.
     * @param p the point to subtract from this point.
     * @return a vector representing the result of the subtraction.
     */
    public Vector subtract(Point p) {
        return new Vector(this.coords.subtract(p.coords));
    }

    /**
     * Adds the specified vector to this point and returns a new point.
     * @param v the vector to add to this point.
     * @return a new point resulting from the addition of the vector to this point.
     */
    public Point add(Vector v) {
        return new Point(this.coords.add(v.coords));
    }

    /**
     * Calculates the squared distance between this point and another specified point.
     * @param p the other point to calculate the squared distance to.
     * @return the squared distance between this point and the specified point.
     */
    public double distanceSquared(Point p) {
        double x = this.coords.d1() - p.coords.d1();
        double y = this.coords.d2() - p.coords.d2();
        double z = this.coords.d3() - p.coords.d3();
        return x * x + y * y + z * z;
    }

    /**
     * Calculates the distance between this point and another specified point.
     * @param p the other point to calculate the distance to.
     * @return the distance between this point and the specified point.
     */
    public double distance(Point p) {
        return Math.sqrt(this.distanceSquared(p));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.coords.equals(other.coords);
    }

    @Override
    public String toString() {
        return "Point: " + coords.toString();
    }
}
