package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Plane class represents a 2D plane of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Plane implements Geometry {
    /**
     * A point on the geometric plane used to define the plane's position
     * in 3D Cartesian space.
     */
    private final Point p;

    /**
     * The (normalized) vector represents the direction of the plane.
     */
    private final Vector v;

    /**
     * Constructs a Plane object using three points in 3D space.
     * The three points define the direction and position of the plane.
     * and calculates the normal
     * @param p1 the first point on the plane.
     * @param p2 the second point on the plane.
     * @param p3 the third point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.p = p1;
        this.v = p2.subtract(p1).crossProduct(p3.subtract(p1)).normalize();
    }

    /**
     * Constructs a Plane object using a given point and a vector.
     * The point specifies a location on the plane, and the vector defines the plane's normal direction.
     * The normal vector is normalized during this initialization.
     * @param p a Point object representing a position on the plane.
     * @param v a Vector object representing the normal to the plane.
     */
    public Plane(Point p, Vector v) {
        this.p = p;
        this.v = v.normalize();
    }

    @Override
    public Vector getNormal(Point p) {
        return this.v;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Point that represents the ray's head
        final Point P0 = ray.getPoint(0);

        // in case the ray's head is the reference point in the plane, there are no intersections
        if(P0.equals(this.p))
            return null;

        // numerator for the formula
        final double num = this.v.dotProduct(this.p.subtract(P0));
        // denominator for the formula
        final double den = this.v.dotProduct(ray.getVector());
        // in case ray is parallel to the plane
        if (isZero(den))
            return null;

        final double t = alignZero(num / den);
        // if (0 ≥ t) there are no intersections
        if (t > 0)
            return List.of(ray.getPoint(t));
        return null;
    }
}
