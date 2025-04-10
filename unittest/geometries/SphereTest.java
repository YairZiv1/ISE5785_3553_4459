package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Sphere
 * @author Yair Ziv and Amitay Yosh'i
 */
class SphereTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        /** A point for tests at (-3,-2,3) */
        final Point P = new Point(-3,-2,3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the triangle's normal to the expected result.
        /** A sphere for test */
        final Sphere SPHERE = new Sphere(4, new Point(1,-2,3));

        // ensure there are no exceptions
        assertDoesNotThrow(() -> SPHERE.getNormal(P), "");
        assertEquals(new Vector(-1,0,0), SPHERE.getNormal(P),
                "ERROR: The calculation of the normal isn't as excepted");
    }
}