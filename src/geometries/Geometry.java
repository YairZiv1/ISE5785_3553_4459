package geometries;

import primitives.*;

/**
 * Class Geometry is the basic interface representing a general geometric shape
 * of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system
 * @author Yair Ziv and Amitay Yosh'i.
 */
public interface Geometry extends Intersectable {
    /**
     * Calculates the normal vector to the geometric object at the given point.
     * @param p the point on the surface of the geometric object where the normal is to be calculated
     * @return the normal vector to the geometric object at the given point.
     */
    public abstract Vector getNormal(Point p);
}
