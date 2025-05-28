package primitives;

import geometries.Intersectable.Intersection;
import java.util.List;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in a Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class Ray {
    /**
     * A small constant used represents the amount of ray origin movement for shadow rays
     */
    private static final double DELTA = 0.1;

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
     * Construct a ray with starting point, direction and normal.
     * This constructor ensures that the reflected and refracted rays
     * won't intersect the geometry again (avoid self-intersections).
     * @param p the starting point of the ray
     * @param v the direction vector of the ray
     * @param normal the surface normal at the point of origin
     */
    public Ray(Point p, Vector v, Vector normal) {
        this.v = v.normalize();
        double vNormal = v.dotProduct(normal);
        if (Util.isZero(vNormal))
            this.p = p;
        else
            this.p = p.add(normal.scale(vNormal > 0 ? DELTA : -DELTA));
    }

    /**
     * Get function for the point that represents the ray.
     * Note: when {@param t} and the vector (v) is very small (but not zero), and they are scaled together
     * the result is the zero vector.
     * Therefore, we are catching the exception in that case.
     * @param t for getting a point on the Ray (not just the head point)
     * @return p the starting point of the ray or p scaled.
     */
    public Point getPoint(double t) {
        if (Util.isZero(t))
            return p;
        try {
            return p.add(v.scale(t));
        } catch (IllegalArgumentException e) {
            return p;
        }
    }

    /**
     * Calculating which of the intersections in a given list is the closest to the head of the ray.
     * If the list is null or empty, returns null.
     * @param intersections list of intersections.
     * @return the closest intersection to the head of the ray from the list of intersections, or null if none exist.
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        // if the list is not initialized, there is no intersection that is the closest - return null.
        if (intersections == null)
            return null;

        // initialized to null - if the list is empty will stay null.
        Intersection closestIntersection = null;

        for(Intersection intersection : intersections) {
            // if this is the first intersection - change the intersection,
            // or it's closer than the closest intersection for now, so this is the new closest intersection.
            if (closestIntersection == null ||
                    p.distanceSquared(intersection.point) < p.distanceSquared(closestIntersection.point))
                closestIntersection = intersection;
        }

        return closestIntersection;
    }

    /**
     * Calling a function that finds the closest intersection to the head of the ray.
     * When this method calls that function - list of intersections is made, the geometry is null.
     * @param points list of points for check the closest to the head of the ray.
     * @return the closest point to the head of the ray from the list of points, or null if none exist.
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestIntersection(
                        points.stream().map(p -> new Intersection(null, p)).toList()).point;
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
