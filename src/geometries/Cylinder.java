package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * The Cylinder class represents a 3D cylinder object of Euclidean geometry in a Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Cylinder extends Tube {
    /**
     * Represents the height of the cylinder.
     */
    private final double height;

    /**
     * The bottom base of the cylinder, represented as a circle.
     */
    private final Circle bottomBase;

    /**
     * The top base of the cylinder, represented as a circle.
     */
    private final Circle topBase;

    /**
     * Constructs a Cylinder object with the specified radius, ray, and height.
     * @param radius the radius of the cylinder.
     * @param ray    the ray of the cylinder.
     * @param height the height of the cylinder.
     */
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this.height = height;

        // Calculates the bottom circle of the cylinder
        Point bottomCenter = ray.getPoint(0);
        bottomBase = new Circle(bottomCenter, radius, getNormal(bottomCenter));

        // Calculates the top circle of the cylinder
        Point topCenter = bottomCenter.add(ray.getVector().scale(height));
        topBase = new Circle(topCenter, radius, getNormal(topCenter));
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

        // In case, the given Point is on the lower base.
        if (Util.isZero(t))
            return rayVector.scale(-1);
        // In case, the given Point is on the upper base.
        else if (t == height)
            return rayVector;
        // In case, the Point is on the side - use super.
        else
            return super.getNormal(p);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point baseCenter = ray.getPoint(0);
        List<Intersection> intersections = null;
        var list = super.calculateIntersectionsHelper(ray, maxDistance);
        if (list != null)
            for (Intersection intersection : list) {
                double distance = Util.alignZero(intersection.point.subtract(baseCenter).dotProduct(ray.getVector()));
                if (distance > 0 && Util.alignZero(distance - height) < 0) {
                    if (intersections == null)
                        intersections = new LinkedList<>();
                    intersections.add(new Intersection(this, intersection.point));
                }
            }

        // Check intersection with bottom base
        intersections = getIntersections(ray, bottomBase, intersections, maxDistance);

        // Check intersection with top base
        intersections = getIntersections(ray, topBase, intersections, maxDistance);

        return intersections;
    }

    /**
     * Helper method to calculate intersections between a ray and a circular base of the cylinder.
     * @param ray the ray to intersect
     * @param circle the circular base (either bottom or top)
     * @param intersections  the existing list of intersections
     * @param maxDistance the maximum allowed distance from the ray's origin to an intersection point
     * @return an updated list of intersections including any new intersection with the given circle
     */
    private List<Intersection> getIntersections(Ray ray, Circle circle, List<Intersection> intersections, double maxDistance) {
        // Find intersections with the circles
        var list = circle.calculateIntersections(ray, maxDistance);

        if (list != null) {
            if (intersections == null)
                intersections = new LinkedList<>();
            intersections.add(new Intersection(this, list.getFirst().point));
        }
        return intersections;
    }
}
