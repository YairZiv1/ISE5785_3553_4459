package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Plane class represents a 2D plane of Euclidean geometry in a Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */

public class Plane extends Geometry {
    /**
     * A point on the geometric plane used to define the plane's position
     * in 3D Cartesian space.
     */
    private final Point head;

    /**
     * The (normalized) vector represents the direction of the plane.
     */
    private final Vector normal;

    /**
     * Constructs a Plane object using three points in 3D space.
     * The three points define the direction and position of the plane,
     * and calculate the normal
     * @param p1 the first point on the plane.
     * @param p2 the second point on the plane.
     * @param p3 the third point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.head = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructs a Plane object using a given point and a vector.
     * The point specifies a location on the plane, and the vector defines the plane's normal direction.
     * The normal vector is normalized during this initialization.
     * @param p a Point object representing a position on the plane.
     * @param v a Vector object representing the normal to the plane.
     */
    public Plane(Point p, Vector v) {
        this.head = p;
        this.normal = v.normalize();
    }

    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getVector();

        // in case the ray's head is the reference point in the plane, there are no intersections
        if(rayPoint.equals(head))
            return null;

        // numerator for the formula
        final double numerator = normal.dotProduct(head.subtract(rayPoint));
        // denominator for the formula
        final double denominator = normal.dotProduct(rayVector);
        // in case ray is parallel to the plane
        if (isZero(denominator))
            return null;

        final double t = numerator / denominator;

        // if (0 ≥ t) or (maxDistance < t) there are no intersections
        return (alignZero(t) > 0 && alignZero(t - maxDistance) <= 0) ?
                List.of(new Intersection(this, ray.getPoint(t))) : null;
    }
}
