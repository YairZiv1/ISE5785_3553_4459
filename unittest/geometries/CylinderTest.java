package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Cylinder
 * @author Yair Ziv and Amitay Yosh'i
 */
class CylinderTest {

    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // A cylinder for test
        final Cylinder cylinder =
                new Cylinder(5, new Ray(new Point(1,2,3), new Vector(0,1,0)), 10);
        // A vector for the excepted normal on the lower base of the cylinder
        final Vector exceptedLowerNormal = new Vector(0,-1,0);
        // A vector for the excepted normal on the upper base of the cylinder
        final Vector exceptedUpperNormal = new Vector(0,1,0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the cylinder's normal to the expected result when the point is on the envelope.
        // A point for tests at (5,7,6)
        final Point p576 = new Point(5,7,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p576), "");
        assertEquals(new Vector(0.8,0,0.6), cylinder.getNormal(p576),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC02: Test that compares the cylinder's normal to the expected result when the point is on the lower base.
        // A point for tests at (2,2,2)
        final Point p222 = new Point(2,2,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p222), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p222),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC03: Test that compares the cylinder's normal to the expected result when the point is on the upper base.
        // A point for tests at (2,12,2)
        final Point p2_12_2 = new Point(2,12,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p2_12_2), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p2_12_2),
                "ERROR: The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Checks the cylinder's normal when the point is the center of the lower base.
        // A point for tests at (1,2,3)
        final Point p123 = new Point(1,2,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p123), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p123),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC12: Checks the cylinder's normal when the point is the center of the upper base.
        // A point for tests at (1,12,3)
        final Point p1_12_3 = new Point(1,12,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p1_12_3), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p1_12_3),
                "ERROR: The calculation of the normal isn't as excepted");


        // TC13: Checks the cylinder's normal when the point is between the lower base and the envelope.
        // A point for tests at (5,2,6)
        final Point p526 = new Point(5,2,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p526), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p526),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC14: Checks the cylinder's normal when the point is between the upper base and the envelope.
        // A point for tests at (5,12,6)
        final Point p5_12_6 = new Point(5,12,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p5_12_6), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p5_12_6),
                "ERROR: The calculation of the normal isn't as excepted");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
    }
}