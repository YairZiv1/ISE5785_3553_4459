package geometries;

import primitives.*;

import java.util.List;

/**
 * The Intersectable abstract class represents shapes that can be intersected by ray
 * @author Yair Ziv and Amitay Yosh'i.
 */
public abstract class Intersectable {
    /**
     * The Intersection class is to associate intersection points with intersecting geometries.
     */
    public static class Intersection {
        /**
         * A geometry intersected
         */
        public final Geometry geometry;
        /**
         * An intersection point
         */
        public final Point point;

        /**
         * Constructor for initialization Intersection fields
         * @param geometry a geometry for initialization
         * @param point a point for initialization
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other)
                    && geometry.equals(other.geometry) && point.equals(other.point);
        }

        @Override
        public String toString() {
            return "geometry=" + geometry + ", point=" + point;
        }
    }

    /**
     * Function that called from a geometry shape and calculates the intersections with a given ray.
     * This method cannot be overridden
     * @param ray the given ray that we want to calculate the intersections with
     * @return List of the shape's intersections with ray (Converting the intersection list to a points list)
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Calculates the intersection between a ray and a geometry, every geometry class implementing it.
     * Why not do everything in the method calculateIntersections?
     * To use the NVI (non-virtual-interface) pattern.
     * The method should be private according to NVI, but java does not allow private abstract methods.
     * @param ray the ray that make the intersection
     * @return a list of the intersection and the geometry that intersected
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * This method is calling to calculateIntersectionsHelper.
     * This method is can't be overridden (according to NVI pattern)
     * and now - no geometry class can change the general behavior.
     * @param ray the ray that make the intersection
     * @return a list of the intersection and the geometry that intersected
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }
}
