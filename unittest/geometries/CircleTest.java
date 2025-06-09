package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Circle
 * @author Yair Ziv and Amitay Yosh'i
 */
class CircleTest {
    /** Default constructor to satisfy JavaDoc generator */
    CircleTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Circle#getNormal(Point)}
     */
    @Test
    void getNormal() {
        // A point for tests at (0, 0, 1)
        final Point p001 = new Point(0, 0, 1);
        // A point for tests at (1, 0, 1)
        final Point p101 = new Point(1, 0, 1);
        // A point for tests at (0, 1, 1)
        final Point p011 = new Point(0, 1, 1);

        // Vectors for tests
        final Vector v1 = p001.subtract(p101);
        final Vector v2 = p011.subtract(p101);


        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the triangle's normal to the expected result.
        // A circle for test
        final Circle circle = new Circle(1, p001, new Vector(0, 0, 1));
        // A vector for the circle's normal
        final Vector normal = circle.getNormal(p001);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> circle.getNormal(p101), "");
        assertEquals(0, v1.dotProduct(normal), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, v2.dotProduct(normal), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, normal.length(), DELTA,
                "ERROR: The normal isn't normalized");
    }

    /**
     * Test method for {@link Circle#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // A point for tests at (0, 0, 1)
        final Point p001 = new Point(0, 0, 1);
        // A point for the ray head
        Point rayHead = new Point(0, 0, 2);

        // A vector used in some test cases to (0,0,1)
        final Vector v001 = new Vector(0,0,1);

        // A circle for tests
        final Circle circle = new Circle(1, p001, v001);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is inside the circle (1 point)
        final var result01 = circle.findIntersections(new Ray(rayHead, new Vector(0.5, 0.5, -1)));
        assertNotNull(result01, "Can't be empty list");
        assertEquals(1, result01.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.5,0.5,1)), result01, "Ray inside circle");

        // TC02: Ray is outside the circle (0 points)
        assertNull(circle.findIntersections(new Ray(rayHead, new Vector(-0.5, -0.5, 1))),
                "Ray outside triangle against edge");

        // =============== Boundary Values Tests ==================
        // TC11: Ray is on the edge of the circle
        assertNull(circle.findIntersections(new Ray(rayHead, new Vector(1, 0, -1))),
                "Ray on edge");

        // TC12: Ray is in the center of the circle
        final var result12 = circle.findIntersections(new Ray(rayHead, new Vector(0, 0, -1)));
        assertNotNull(result12, "Can't be empty list");
        assertEquals(1, result12.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0,0,1)), result12, "Ray in circle's center");
    }

    /**
     * Test method for {@link Circle#calculateIntersectionsHelper(Ray, double)}
     */
    @Test
    void calculateIntersectionsHelper() {
        // A circle for tests
        final Circle circle = new Circle(2, new Point(0,1,0), Vector.AXIS_X);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the circle
        assertNull(circle.calculateIntersections(new Ray(new Point(-3, 0, 0), Vector.AXIS_X), 2),
                "ray stops before the circle");

        // TC02: Ray crosses the circle
        final var result02 = circle.calculateIntersections(
                new Ray(new Point(-1, 0, 0), Vector.AXIS_X), 2);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        // TC03: Ray starts after the circle
        assertNull(circle.calculateIntersections(new Ray(new Point(1, 0, 0), Vector.AXIS_X), 2),
                "ray starts after the circle");

        // =============== Boundary Values Tests ==================
        // TC11: Ray "stops" at the circle
        final var result11 = circle.calculateIntersections(
                new Ray(new Point(-2, 0, 0), Vector.AXIS_X), 2);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
    }
}