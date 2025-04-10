package geometries;

import primitives.Point;

/**
 * The Triangle class represents a two-dimensional triangle of Euclidean geometry in  Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
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
}
