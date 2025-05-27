package primitives;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Ray
 * @author Yair Ziv and Amitay Yosh'i
 */
class RayTest {
    /** Default constructor to satisfy JavaDoc generator */
    RayTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        // A ray for tests
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

    /**
     * Test method for {@link Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        // A ray for tests
        final Ray ray = new Ray(new Point(1,1,1), new Vector(0,0,1));

        // A point for tests at (1,1,2)
        final Point p112 = new Point(1, 1, 2);
        // A point for tests at (1,1,3)
        final Point p113 = new Point(1, 1, 3);
        // A point for tests at (1,1,4)
        final Point p114 = new Point(1, 1, 4);
        // A point for tests at (1,1,5)
        final Point p115 = new Point(1, 1, 5);
        // A point for tests at (1,1,6)
        final Point p116 = new Point(1, 1, 6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: The closest point is in the middle of the list
        assertEquals(p112, ray.findClosestPoint(Arrays.asList(p113, p115, p112, p116, p114)),
                "ERROR: Didn't return the closest point");

        // =============== Boundary Values Tests ==================
        // TC11: Empty list
        assertNull(ray.findClosestPoint(Collections.emptyList()), "ERROR: Didn't return null for empty list");

        // TC12: The closest point is in the start of the list
        assertEquals(p112, ray.findClosestPoint(Arrays.asList(p112, p116, p114)),
                "ERROR: Didn't return the closest point when it's the first in list");

        // TC13: The closest point is at the end of the list
        assertEquals(p112, ray.findClosestPoint(Arrays.asList(p113, p115, p112)),
                "ERROR: Didn't return the closest point when it's the last in list");
    }
}