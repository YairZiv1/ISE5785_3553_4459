package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * The Cylinder class represents a 3D cylinder object of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Cylinder extends Tube {
    /**
     * Represents the height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a Cylinder object with the specified radius, ray, and height.
     * @param radius the radius of the cylinder.
     * @param ray    the ray of the cylinder.
     * @param height the height of the cylinder.
     */
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p) {
        // A variable that contains the Point of the Ray.
        final Point rayPoint = ray.getPoint(0);
        // A variable that contains the Vector of the Ray
        final Vector rayVector = ray.getVector();

        // In case the given Point is the same as the Ray's Point
        if (p.equals(rayPoint))
            return rayVector.scale(-1);

        final double t = rayVector.dotProduct(p.subtract(rayPoint));

        // In case the given Point is on the lower base.
        if (Util.isZero(t))
            return rayVector.scale(-1);
        // In case the given Point is on the upper base.
        else if (t == height)
            return rayVector;
        // In case the Point is on the side - use super.
        else
            return super.getNormal(p);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
