package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

/**
 * The Tube class represents a 3D tube of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube, which is represented by a ray in 3D space.
     */
    protected final Ray ray;

    /**
     * Constructs a Tube object with a specified radius and central axis ray.
     *
     * @param radius the radius of the tube; must be a positive value.
     * @param ray    the central axis of the tube, represented as a Ray object.
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.ray = ray;
    }

    @Override
    public Vector getNormal(Point p) {
        // A variable that contains the Point of the Ray.
        final Point P0 = ray.getPoint(0);
        // A variable that contains the Vector of the Ray
        final Vector V0 = ray.getVector();
        /**
         * Help variable for calculating the normal.
         * If it is 0 - this means that the given point (p) is on the circle whose center is the point of the ray.
         * This means that the angle between -
         * the ray vector and the vector between the point of the ray and the given point is 90, and the scalar is 0.
         */
        final double SCALAR = V0.dotProduct(p.subtract(P0));
        /**
         * In the case where the scalar is 0 -
         * it turns out that we will be doing a vector product with the scalar 0, and the can't a vector 0.
         * So we ensure that it won't happen
         */
        if (Util.isZero(SCALAR))
            return p.subtract(P0).normalize();
        else
            // According to the calculation we learned
            return p.subtract(ray.getPoint(SCALAR)).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
