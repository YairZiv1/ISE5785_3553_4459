package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Tube
 * @author Yair Ziv and Amitay Yosh'i
 */
class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        /** A tube for test */
        final Tube TUBE = new Tube(5, new Ray(new Point(1,2,3), new Vector(0,1,0)));
        /** A vector for the excepted normal */
        final Vector EXCEPTED_NORMAL = new Vector(0.8,0,0.6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the tube's normal to the expected result.
        /** A point for tests at (5,7,6) */
        final Point P1 = new Point(5,7,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> TUBE.getNormal(P1), "");
        assertEquals(EXCEPTED_NORMAL, TUBE.getNormal(P1),
                "ERROR: The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Test that compares the tube's normal to the expected result
        // when the point is opposite the head of the ray (Point that closest to the head).
        /** A point for tests at (5,2,6) */
        final Point P2 = new Point(5,2,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> TUBE.getNormal(P2), "");
        assertEquals(EXCEPTED_NORMAL, TUBE.getNormal(P2),
                "ERROR: The calculation of the normal isn't as excepted");
    }

    /**
     * Test method for {@link Tube#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
    }
}