package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Sphere class represents a 3D geometrical body of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Sphere extends RadialGeometry {
    /**
     * Represents the center of the sphere.
     */
    private final Point center;

    /**
     * Constructs a new Sphere object with the specified radius and center point.
     * @param radius the radius of the sphere; must be a positive value.
     * @param center the center point of the sphere in 3D space.
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }
}
