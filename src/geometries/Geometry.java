package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Class Geometry is the basic abstract class representing a general geometric shape
 * of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public abstract class Geometry {
    /**
     * Calculates the normal vector to the geometric object at the given point.
     * @param p the point on the surface of the geometric object where the normal is to be calculated
     * @return the normal vector to the geometric object at the given point.
     */
    public abstract Vector getNormal(Point p);
}
