package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        /** A point for tests at (5,7,6) */
        final Point P = new Point(5,7,6);
        /** A vector for the sphere's axis */
        final Vector AXIS = new Vector(0,1,0);
        /** A vector for the excepted normal */
        final Vector EXCEPTED_NORMAL = new Vector(-0.8,0,-0.6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the tube's normal to the expected result.
        /** A tube for test */
        final Tube TUBE = new Tube(5, new Ray(new Point(1,2,3), AXIS));

        // ensure there are no exceptions
        assertDoesNotThrow(() -> TUBE.getNormal(P), "");
        assertEquals(EXCEPTED_NORMAL, TUBE.getNormal(P),
                "ERROR: The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Test that compares the tube's normal to the expected result when the point is opposite the rayhead.
        /** A tube for test */
        final Tube TUBE2 = new Tube(5, new Ray(new Point(1,7,3), AXIS));

        // ensure there are no exceptions
        assertDoesNotThrow(() -> TUBE2.getNormal(P), "");
        assertEquals(EXCEPTED_NORMAL, TUBE2.getNormal(P),
                "ERROR: The calculation of the normal isn't as excepted");
    }
}