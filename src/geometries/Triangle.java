package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

/**
 * The Triangle class represents a two-dimensional triangle of Euclidean geometry in  Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class Triangle extends Polygon {
    /**
     * Constructs a Triangle object using three points in 3D space.
     * @param p1 the first point on the triangle.
     * @param p2 the second point on the triangle.
     * @param p3 the third point on the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // test the intersections with triangle’s plane
        final var intersections = plane.findIntersections(ray);
        if (intersections == null)
            return null;

        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getVector();

        // vector1, vector2, vector3 can't be the ZERO Vector because it happens only if rayPoint = P1/P2/P3,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        final Vector vector1 = vertices.get(0).subtract(rayPoint);
        final Vector vector2 = vertices.get(1).subtract(rayPoint);
        final Vector vector3 = vertices.get(2).subtract(rayPoint);

        // normal1, normal2, normal3 can't be the ZERO Vector because it happens only if:
        // vector1 and vector2 or vector2 and vector3 or vector3 and vector1
        // are on the same line, which means rayPoint is on one of the triangle's edges,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        final Vector normal1 = vector1.crossProduct(vector2).normalize();
        final Vector normal2 = vector2.crossProduct(vector3).normalize();
        final Vector normal3 = vector3.crossProduct(vector1).normalize();

        final double s1 = alignZero(rayVector.dotProduct(normal1));
        final double s2 = alignZero(rayVector.dotProduct(normal2));
        final double s3 = alignZero(rayVector.dotProduct(normal3));

        // the point is inside the triangle only if s1, s2 and s3 have the same sign and none of them is 0
        if ((s1>0 && s2>0 && s3>0) || (s1<0 && s2<0 && s3<0))
            return intersections;

        return null;
    }
}
