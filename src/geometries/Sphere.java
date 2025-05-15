package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

/**
 * The Sphere class represents a 3D geometrical body of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class Sphere extends RadialGeometry {
    /**
     * Represents the center of the sphere.
     */
    private final Point center;

    /**
     * Constructs a new Sphere object with the specified radius and center point.
     * @param center the center point of the sphere in 3D space.
     * @param radius the radius of the sphere; must be a positive value.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);

        // in case the ray's head is the sphere's center, we calculate the one intersection directly
        if(rayPoint.equals(center))
            return List.of(new Intersection(this, ray.getPoint(radius)));

        final Vector u = center.subtract(rayPoint);
        final double tm = ray.getVector().dotProduct(u);
        final double d = Math.sqrt(u.lengthSquared() - tm * tm);
        // if (d ≥ r) there are no intersections
        if( alignZero(d - radius) > 0)
            return null;

        final double th = Math.sqrt(radius * radius - d * d);
        // in case the ray is tangent to the sphere, there are no intersections
        if(isZero(th))
            return null;
        final double t1 = alignZero(tm - th);
        final double t2 = alignZero(tm + th);

        // 2 intersections
        if(t1 > 0 && t2 > 0) {
            Intersection intersection1 = new Intersection(this, ray.getPoint(t1));
            Intersection intersection2 = new Intersection(this, ray.getPoint(t2));
            return List.of(intersection1, intersection2);
        }
        // 1 intersection
        else if(t1 > 0)
            return List.of(new Intersection(this, ray.getPoint(t1)));
        // 1 intersection
        else if(t2 > 0)
            return List.of(new Intersection(this, ray.getPoint(t2)));
        // 0 intersections
        else
            return null;
    }
}
