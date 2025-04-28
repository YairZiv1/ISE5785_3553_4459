package geometries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.*;

import primitives.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan.
 */
public class Polygon implements Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays.
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal(vertices[0]);
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // test the intersections with polygon's plane
        final var intersections = plane.findIntersections(ray);
        if (intersections == null)
            return null;

        // Point that represents the ray's head
        final Point rayPoint = ray.getPoint(0);
        // Vector that represents the ray's axis
        final Vector rayVector = ray.getVector();
        // number that represents the size of vertices
        final int size_vertices = vertices.size();

        // Array of the v vectors in the formula
        List<Vector> vectorsV = new ArrayList<>();
        // Array of the dot-product of the n vectors with the vector of ray in the formula
        double[] s = new double[size_vertices];

        // These vectors can't be the ZERO Vector because it happens only if rayPoint is one of the vertices,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        for (Point vertex : vertices)
            vectorsV.add(vertex.subtract(rayPoint));

        // These vectors can't be the ZERO Vector because it happens only if two of vectorsV
        // are on the same line, which means rayPoint is on one of the triangle's edges,
        // which means the ray begins at the plane and there are no intersections with the plane at all,
        // so we would have exit this method already because of the first condition
        Vector vector1;
        Vector vector2;
        Vector normal;
        for (int i=0; i < size_vertices; ++i) {
            if (i == size_vertices - 1) {
                vector1 = vectorsV.getFirst();
                vector2 = vectorsV.getLast();
            }
            else {
                vector1 = vectorsV.get(i + 1);
                vector2 = vectorsV.get(i);
            }

            normal = vector1.crossProduct(vector2);
            s[i] = alignZero(rayVector.dotProduct(normal));

            if (i != 0 && s[i] * s[i-1] <= 0)
                return null;
        }
        return intersections;
    }
}
