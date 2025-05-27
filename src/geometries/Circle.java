package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;


/**
 * Class representing a circle in 3D space.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Circle extends RadialGeometry {
    /**
     * The center point of the circle
     */
    private final Point center;

    /**
     * The circle lay's in this plane.
     */
    protected final Plane plane;


    /**
     * Constructor to create a circle.
     * @param center the center point of the circle
     * @param radius the radius of the circle
     * @param normal the normal to the circle's plane
     */
    public Circle(Point center, double radius, Vector normal) {
        super(radius);
        this.center = center;
        this.plane = new Plane(center, normal);
    }

    @Override
    public Vector getNormal(Point point) { return plane.getNormal(point); }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = plane.calculateIntersections(ray, maxDistance);

        // If there is no intersection with the plane - of course that in the circle there are no intersection.
        // But if there is an intersection - check if the intersection is on the circle.
        if (intersections != null && Util.alignZero(center.distance(intersections.getFirst().point) - radius) < 0)
            return List.of(new Intersection(this, intersections.getFirst().point));
        return null;
    }
}