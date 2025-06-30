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
     * Represents the center point of the circle
     */
    private final Point center;
    /**
     * Associated plane in which the circle lays.
     */
    protected final Plane plane;

    /**
     * Constructor to create a circle.
     * @param radius the radius of the circle.
     * @param center the center point of the circle.
     * @param normal the normal to the circle's plane.
     */
    public Circle(double radius, Point center, Vector normal) {
        super(radius);
        this.center = center;
        this.plane = new Plane(center, normal);
    }

    @Override
    protected Geometry moveHelper(Vector offset) {
        return new Circle(radius, center.add(offset), this.getNormal(this.center));
    }

    @Override
    public Vector getNormal(Point point) { return plane.getNormal(point); }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Test if the ray intersects the bounding box of the circle
        if (boundingBox != null && boundingBox.isNotIntersected(ray)) return null;

        // test the intersections with circle’s plane
        // we prefer to use the helper method so that we already check the distance
        final var intersections = plane.calculateIntersections(ray, maxDistance);
        if (intersections == null)
            return null;

        if (Util.alignZero(center.distance(intersections.getFirst().point) - radius) < 0)
            return List.of(new Intersection(this, intersections.getFirst().point));

        return null;
    }

    @Override
    protected BoundingBox calculateBoundingBox() {
        return new BoundingBox(
                new Point(center.getX() - radius, center.getY() - radius, center.getZ() - radius),
                new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius)
        );
    }
}