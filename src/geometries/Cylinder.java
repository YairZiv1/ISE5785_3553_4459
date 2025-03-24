package geometries;

import primitives.Point;
import primitives.Ray;
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
        return null;
    }
}
