package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

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
        final Point P0 = ray.getPoint(0);
        // A variable that contains the Vector of the Ray
        final Vector V0 = ray.getVector();

        // In case the given Point is the same as the Ray's Point
        if (p.equals(P0))
            return V0.scale(-1);

        final double SCALAR = V0.dotProduct(p.subtract(P0));

        // In case the given Point is on the lower base.
        if (Util.isZero(SCALAR))
            return V0.scale(-1);
        // In case the given Point is on the upper base.
        else if (SCALAR == height)
            return V0;
        // In case the Point is on the side - use super.
        else
            return super.getNormal(p);
    }
}
