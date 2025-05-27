package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Triangle
 * @author Yair Ziv and Amitay Yosh'i
 */
class TriangleTest {
    /** Default constructor to satisfy JavaDoc generator */
    TriangleTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // A point for tests at (1,2,3)
        final Point p123 = new Point(1, 2, 3);
        // A point for tests at (0,3,-2)
        final Point p03m2 = new Point(0, 3, -2);
        // A point for tests at (5, 6, 3)
        final Point p563 = new Point(5, 6, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the triangle's normal to the expected result.
        // A triangle for test
        final Triangle triangle = new Triangle(p123, p03m2, p563);
        // A vector for the triangle's normal
        final Vector normal = triangle.getNormal(p123);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> triangle.getNormal(p123), "");
        assertEquals(0, p123.subtract(p03m2).dotProduct(normal), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, p123.subtract(p563).dotProduct(normal), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, normal.length(), DELTA,
                "ERROR: The normal isn't normalized");
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // A triangle for test
        final Triangle triangle = new Triangle(
                new Point(0,0,1), new Point(-1,0,0), new Point(-1,1,0));

        // A vector used in some test cases to (0,0,1)
        final Vector v001 = new Vector(0,0,1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is inside the triangle (1 point)
        final var result01 = triangle.findIntersections(new Ray(new Point(-0.7,0.5,0), v001));
        assertNotNull(result01, "Can't be empty list");
        assertEquals(1, result01.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-0.7,0.5,0.3)), result01, "Ray inside triangle");

        // TC02: Ray is outside the triangle against edge (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-2,0.5,-2), v001)),
                "Ray outside triangle against edge");

        // TC03: Ray is outside the triangle against vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-2,3,-2), v001)),
                "Ray outside triangle against vertex");

        // =============== Boundary Values Tests ==================
        // TC11: Ray is on the edge
        assertNull(triangle.findIntersections(new Ray(new Point(-0.5,0.5,0), v001)),
                "Ray on edge");

        // TC12: Ray is on the vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0,0,0.5), v001)),
                "Ray on vertex");

        // TC13: Ray is on the edge's continuation
        assertNull(triangle.findIntersections(new Ray(new Point(-2,2,-2), v001)),
                "Ray on edge's continuation");
    }

    /**
     * Test method for {@link Triangle#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // A point for tests at (0,-1,1)
        final Point p0m11 = new Point(0, -1, 1);
        // A point for tests at (0,-1,-1)
        final Point p0m1m1 = new Point(0, -1, -1);
        // A point for tests at (0,1,0)
        final Point p010 = new Point(0, 1, 0);
        // A plane for test
        final Triangle triangle = new Triangle(p0m11, p0m1m1, p010);

        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the triangle
        assertNull(triangle.calculateIntersections(new Ray(new Point(-3, 0, 0), v100), 2),
                "ray stops before the triangle");

        // TC02: Ray crosses the triangle
        final var result02 = triangle.calculateIntersections(new Ray(new Point(-1, 0, 0), v100), 2);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        // TC03: Ray starts after the triangle
        assertNull(triangle.calculateIntersections(new Ray(new Point(1, 0, 0), v100), 2),
                "ray starts after the triangle");

        // =============== Boundary Values Tests ==================
        // TC11: Ray "stops" at the triangle
        final var result11 = triangle.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 2);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
    }
}