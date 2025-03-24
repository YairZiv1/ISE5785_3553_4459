package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
     * @param radius the radius of the tube; must be a positive value.
     * @param ray the central axis of the tube, represented as a Ray object.
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.ray = ray;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}
