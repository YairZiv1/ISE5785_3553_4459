package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.LinkedList;
import java.util.List;

/**
 * The Tube class represents a 3D tube of Euclidean geometry in a Cartesian
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
     *
     * @param radius the radius of the tube; must be a positive value.
     * @param ray    the central axis of the tube, represented as a Ray object.
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.ray = ray;
    }

    @Override
    public Vector getNormal(Point p) {
        // A variable that contains the Point of the Ray.
        final Point rayPoint = ray.getPoint(0);
        // A variable that contains the Vector of the Ray
        final Vector rayVector = ray.getVector();

         // Help variable for calculating the normal.
         // If it is 0 - this means that the given point (p) is on the circle whose center is the point of the ray.
         // This means that the angle between
         // the ray vector and the vector between the point of the ray and the given point is 90, and the scalar is 0.
        final double t = rayVector.dotProduct(p.subtract(rayPoint));

        // In the case where the scalar is 0,
        // it turns out that we will be doing a vector product with scalar 0, and the can't a vector 0.
        // So we ensure that it won't happen
        if (Util.isZero(t))
            return p.subtract(rayPoint).normalize();
        else
            // According to the calculation, we learned
            return p.subtract(ray.getPoint(t)).normalize();
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // The head of the intersecting ray.
        Point rayHead = ray.getPoint(0);
        // The head of the ray that represents the tube.
        Point tubeHead = ray.getPoint(0);

        // The direction of the intersecting ray.
        Vector rayDirection = ray.getVector();
        // The direction of the ray that represents the tube.
        Vector axisDirection = ray.getVector();

        // The dot product of the two directions.
        double productDirections = rayDirection.dotProduct(axisDirection);

        // In case the head of the ray is the same as head of the tube's axis:
        if (rayHead.equals(tubeHead)) {
            // In case the direction of the ray orthogonal to the direction of the tube's axis:
            // calculate the intersection directly using the radius (of course, checking the max distance).
            if (Util.isZero(productDirections))
                return Util.alignZero(radius - maxDistance) < 0 ?
                        List.of(new Intersection(this, ray.getPoint(radius))) : null;

            // in case the direction of the ray parallel to the direction of the tube's axis:
            // there are 0 intersections.
            if (rayDirection.equals(axisDirection.scale(productDirections)))
                return null;

            // The distance between the head's and the intersection.
            double t = radius / rayDirection.subtract(axisDirection.scale(productDirections)).length();
            return Util.alignZero(t - maxDistance) < 0 ?
                    List.of(new Intersection(this, ray.getPoint(t))) : null;
        }

        // Vector from the head of the tube's axis to the head of the intersecting ray.
        Vector deltaP = ray.getPoint(0).subtract(tubeHead);
        // The dot product between deltaP and the direction of the tube's axis.
        double dpV = deltaP.dotProduct(axisDirection);

        // Calculating the coefficients of the quadratic equation.
        double a = 1 - productDirections * productDirections;
        double b = 2 * (rayDirection.dotProduct(deltaP) - productDirections * dpV);
        double c = deltaP.lengthSquared() - dpV * dpV - radius * radius;

        if (Util.isZero(a)) {
            if (Util.isZero(b))
                return null;
            return Util.alignZero(-c / b - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(-c / b))) : null;
        }

        // The discriminant
        double discriminant = Util.alignZero(b * b - 4 * a * c);

        // If the discriminant is negative - there are 0 intersections.
        if (discriminant < 0)
            return null;

        double t1 = Util.alignZero(-(b + Math.sqrt(discriminant)) / (2 * a));
        double t2 = Util.alignZero(-(b - Math.sqrt(discriminant)) / (2 * a));

        if (discriminant <= 0)
            return null;

        List<Intersection> intersections = null;

        if (t1 > 0 && Util.alignZero(t1 - maxDistance) < 0) {
            Point point = ray.getPoint(t1);
            if (!point.equals(rayHead)) {
                intersections = new LinkedList<>();
                intersections.add(new Intersection(this, point));
            }
        }
        if (t2 > 0 && Util.alignZero(t2 - maxDistance) < 0) {
            Point point = ray.getPoint(t2);
            if (!point.equals(rayHead)) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.add(new Intersection(this, ray.getPoint(t2)));
            }
        }

        return intersections;
    }
}