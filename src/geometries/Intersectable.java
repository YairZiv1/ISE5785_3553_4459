package geometries;

import primitives.*;

import java.util.List;

/**
 * The Intersectable interface represents shapes that can be intersected by ray
 * @author Yair Ziv and Amitay Yosh'i.
 */
public interface Intersectable {
    /**
     * Function that called from a geometry shape and calculates the intersections with a given ray
     * @param ray the given ray that we want to calculate the intersections with
     * @return List of the shape's intersections with ray
     */
    List<Point> findIntersections(Ray ray);
}
