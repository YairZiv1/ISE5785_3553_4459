package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.*;

/**
 * Testing Plane
 * @author Yair Ziv and Amitay Yosh'i.
 */
class PlaneTest {
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
        /** A point for tests at (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests at (0,3,-2) */
        final Point P2 = new Point(0, 3, -2);
        /** A point for tests at (5, 6, 3) */
        final Point P3 = new Point(5, 6, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the plane's normal to the expected result.
        /** A plane for test */
        final Plane PLANE = new Plane(P1, P2, P3);
        /** A vector for the plane's normal */
        final Vector NORMAL = PLANE.getNormal(P1);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> PLANE.getNormal(P1), "");

        assertEquals(0, P1.subtract(P2).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, P1.subtract(P3).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, NORMAL.length(), DELTA,
                "ERROR: The normal isn't normalized");

        // =============== Boundary Values Tests ==================
        // TC11: Test that checks if the first point of the plane equals to its second point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P1, P3),
                "ERROR: Constructor does not throw an exception if first and second points are equal");
        // TC12: Test that checks if the first point of the plane equals to its third point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P2, P1),
                "ERROR: Constructor does not throw an exception if first and third points are equal");
        // TC13: Test that checks if the second point of the plane equals to its third point.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P2, P2),
                "ERROR: Constructor does not throw an exception if second and third points are equal");
        // TC14: Test that checks if all three points of the plane are equal.
        assertThrows(IllegalArgumentException.class, () -> new Plane(P1, P1, P1),
                "ERROR: Constructor does not throw an exception if all three points are equal");
        // TC15: Test that checks if all three points of the plane are on the same line.
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(P1, new Point(2, 4, 6), new Point(-1, -2, -3)),
                "ERROR: Constructor does not throw an exception if all three points are on the same line");
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        /** A point for tests at (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests at (0,3,-2) */
        final Point P2 = new Point(0, 3, -2);
        /** A point for tests at (5, 6, 3) */
        final Point P3 = new Point(5, 6, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the plane's normal to the expected result.
        /** A plane for test */
        final Plane PLANE = new Plane(P1, P2, P3);
        /** A vector for the plane's normal */
        final Vector NORMAL = PLANE.getNormal(P1);

        assertEquals(0, P1.subtract(P2).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, P1.subtract(P3).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, NORMAL.length(), DELTA,
                "ERROR: The normal isn't normalized");
    }
}