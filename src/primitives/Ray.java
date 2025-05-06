package primitives;

import java.util.List;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class Ray {
    /**
     * The starting point of the ray in 3D space.
     * It is immutable and cannot be changed once the ray is constructed.
     */
    private final Point p;

    /**
     * The direction vector of the ray.
     * Always normalized and represents the direction of the ray.
     * Immutable and cannot be changed once the ray is constructed.
     */
    private final Vector v;

    /**
     * Constructs a Ray object with a specified origin point and direction vector.
     * The direction vector is automatically normalized to ensure unit length.
     * @param p the starting point of the ray.
     * @param v the direction vector of the ray.
     */
    public Ray(Point p, Vector v) {
        this.p = p;
        this.v = v.normalize();
    }

    /**
     * Get function for the point that represents the ray.
     * @param t for getting a point on the Ray (not just the head point)
     * @return p the starting point of the ray or p scaled.
     */
    public Point getPoint(double t) {
        if (Util.isZero(t))
            return p;
        else
            return p.add(v.scale(t));
    }

    /**
     * Calculating which of the points in a given list is the closest to the head of the ray.
     * If the list is null or empty, returns null.
     * @param points list of points.
     * @return the closest point to the head of the ray from the list of points, or null if none exist.
     */
    public Point findClosestPoint(List<Point> points) {
        // if the list is not initialized, there is no point that is the closest - return null.
        if (points == null)
            return null;

        // initialized to null - if the list is empty will stay null.
        Point closestPoint = null;

        for(Point point : points) {
            // if this is the first point we check, or it's closer than the closest point for now,
            // so this is the new closest point.
            if (closestPoint == null || p.distanceSquared(point) < p.distanceSquared(closestPoint))
                closestPoint = point;
        }

        return closestPoint;
    }

    /**
     * Get function for the vector that represents the ray.
     * @return the direction vector of the ray.
     */
    public Vector getVector() {
        return v;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.p.equals(other.p)
                && this.v.equals(other.v);
    }

    @Override
    public String toString() { return "Ray:\n" + p.toString() + " " + v.toString(); }
}
