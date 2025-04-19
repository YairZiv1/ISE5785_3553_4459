package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Ray
 * @author Yair Ziv and Amitay Yosh'i
 */
class RayTest {
    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void getPoint() {
        /** A ray for test */
        final Ray ray = new Ray(new Point(1,1,1), new Vector(0,0,1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray getPoint with positive t
        assertEquals(new Point(1,1,6), ray.getPoint(5),
                "ERROR: Ray getPoint positive t doesn't work correctly");

        // TC02: Ray getPoint with negative t
        assertEquals(new Point(1,1,-4), ray.getPoint(-5),
                "ERROR: Ray getPoint negative t doesn't work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Ray getPoint with t = 0
        assertEquals(new Point(1,1,1), ray.getPoint(0),
                "ERROR: Ray getPoint t = 0 doesn't work correctly");
    }
}