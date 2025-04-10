package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Triangle
 * @author Yair Ziv and Amitay Yosh'i.
 */
class TriangleTest {
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
        /** A point for tests at (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests at (0,3,-2) */
        final Point P2 = new Point(0, 3, -2);
        /** A point for tests at (5, 6, 3) */
        final Point P3 = new Point(5, 6, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the triangle's normal to the expected result.
        /** A triangle for test */
        final Triangle TRIANGLE = new Triangle(P1, P2, P3);
        /** A vector for the triangle's normal */
        final Vector NORMAL = TRIANGLE.getNormal(P1);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> TRIANGLE.getNormal(P1), "");
        assertEquals(0, P1.subtract(P2).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(0, P1.subtract(P3).dotProduct(NORMAL), DELTA,
                "ERROR: The normal isn't orthogonal to one of the plane's vectors");
        assertEquals(1, NORMAL.length(), DELTA,
                "ERROR: The normal isn't normalized");
    }
}