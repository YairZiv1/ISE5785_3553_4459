package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.*;

import java.util.List;

/**
 * Testing Plane
 * @author Yair Ziv and Amitay Yosh'i
 */
class PlaneTest {
    /** Default constructor to satisfy JavaDoc generator */
    PlaneTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructor() {
        // A point for tests at (1,2,3) 
        final Point p123 = new Point(1, 2, 3);
        // A point for tests at (0,3,-2) 
        final Point p03m2 = new Point(0, 3, -2);
        // A point for tests at (5, 6, 3) 
        final Point p563 = new Point(5, 6, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the plane's normal to the expected result.
        // A plane for test
        final Plane PLANE = new Plane(p123, p03m2, p563);
        // A vector for the plane's normal
        final Vector NORMAL = PLANE.getNormal(p123);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> PLANE.getNormal(p123), "");

        assertEquals(0, p123.subtract(p03m2).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, p123.subtract(p563).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, NORMAL.length(), DELTA,
                "ERROR: The normal isn't normalized");

        // =============== Boundary Values Tests ==================
        // TC11: Test that checks if the first point of the plane equals to its second point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(p123, p123, p563),
                "ERROR: Constructor does not throw an exception if first and second points are equal");
        // TC12: Test that checks if the first point of the plane equals to its third point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(p123, p03m2, p123),
                "ERROR: Constructor does not throw an exception if first and third points are equal");
        // TC13: Test that checks if the second point of the plane equals to its third point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(p123, p03m2, p03m2),
                "ERROR: Constructor does not throw an exception if second and third points are equal");
        // TC14: Test that checks if all three points of the plane are equal.
        assertThrows(IllegalArgumentException.class, () -> new Plane(p123, p123, p123),
                "ERROR: Constructor does not throw an exception if all three points are equal");
        // TC15: Test that checks if all three points of the plane are on the same line.
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p123, new Point(2, 4, 6), new Point(-1, -2, -3)),
                "ERROR: Constructor does not throw an exception if all three points are on the same line");
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
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
        // TC01: Test that compares the plane's normal to the expected result.
        // A plane for test
        final Plane PLANE = new Plane(p123, p03m2, p563);
        // A vector for the plane's normal
        final Vector NORMAL = PLANE.getNormal(p123);

        assertEquals(0, p123.subtract(p03m2).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, p123.subtract(p563).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, NORMAL.length(), DELTA,
                "ERROR: The normal isn't normalized");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // The reference point of plane at (0,0,1)
        final Point p001 = new Point(0,0,1);
        // A plane for test
        final Plane plane = new Plane(p001, new Point(-1,0,0), new Point(-1,1,0));

        // A point used in some test cases at (1,1,2)
        final Point p112 = new Point(1,1,2);
        // A point used in some test cases at (1,1,3)
        final Point p113 = new Point(1,1,3);

        // A vector used in some test cases to (0,0,1)
        final Vector v001 = new Vector(0,0,1);
        // A vector used in some test cases to (1,0,1)
        final Vector v101 = new Vector(1,0,1);
        // A vector used in some test cases to (-1,0,1)
        final Vector vm101 = new Vector(-1,0,1);

        // The expected result in some test cases
        final var exp = List.of(p112);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the plane (1 points)
        final var result01 = plane.findIntersections(new Ray(new Point(1,1,1), v001));
        assertNotNull(result01, "Can't be empty list");
        assertEquals(1, result01.size(), "Wrong number of points");
        assertEquals(exp, result01, "Ray intersects plane");

        // TC02: Ray does not intersect the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p113, v001)),
                "Ray does not intersect plane");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray is parallel to the plane (all tests 0 points)
        // TC11: Ray is included in the plane
        assertNull(plane.findIntersections(new Ray(p112, v101)),
                "Ray parallel to plane and included in it");

        // TC12: Ray is not included in the plane
        assertNull(plane.findIntersections(new Ray(p113, v101)),
                "Ray parallel to plane and not included in it");

        // **** Group 2: Ray is orthogonal to the plane
        // TC21: Ray starts before the plane (1 points)
        final var result21 = plane.findIntersections(new Ray(new Point(2,1,1), vm101));
        assertNotNull(result21, "Can't be empty list");
        assertEquals(1, result21.size(), "Wrong number of points");
        assertEquals(exp, result21, "Ray orthogonal to plane and starts before it");

        // TC22: Ray starts in the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p112, vm101)),
                "Ray orthogonal to plane and starts in it");

        // TC23: Ray starts after the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p113, vm101)),
                "Ray orthogonal to plane and starts after it");

        // **** Group 3: Ray begins at the plane
        // TC31: Ray is neither orthogonal nor parallel to the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p112, v001)),
                "Ray begins at plane");

        // **** Group 4: Ray begins at the same point which appears as reference point in the plane
        // TC41: Ray is neither orthogonal nor parallel to the plane (0 points)
        assertNull(plane.findIntersections(new Ray(p001, v001)),
                "Ray begins at reference point of plane");
    }

    /**
     * Test method for {@link Plane#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // A point for tests at (0,1,1)
        final Point p011 = new Point(0, 1, 1);
        // A point for tests at (0,1,0)
        final Point p010 = new Point(0, 1, 0);
        // A point for tests at (0,0,1)
        final Point p001 = new Point(0, 0, 1);
        // A plane for test
        final Plane plane = new Plane(p001, p010, p011);

        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the plane
        assertNull(plane.calculateIntersections(new Ray(new Point(-3, 0, 0), v100), 2),
                "ray stops before the plane");

        // TC02: Ray crosses the plane
        final var result02 = plane.calculateIntersections(new Ray(new Point(-1, 0, 0), v100), 2);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        // TC03: Ray starts after the plane
        assertNull(plane.calculateIntersections(new Ray(new Point(1, 0, 0), v100), 2),
                "ray starts after the plane");

        // =============== Boundary Values Tests ==================
        // TC11: Ray "stops" at the plane
        final var result11 = plane.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 2);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
    }
}