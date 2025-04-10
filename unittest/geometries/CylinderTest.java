package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Cylinder
 * @author Yair Ziv and Amitay Yosh'i.
 */
class CylinderTest {

    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        /** A cylinder for test */
        final Cylinder CYLINDER =
                new Cylinder(5, new Ray(new Point(1,2,3), new Vector(0,1,0)), 10);
        /** A vector for the excepted normal on the lower base of the cylinder */
        final Vector EXCEPTED_LOWER_NORMAL = new Vector(0,-1,0);
        /** A vector for the excepted normal on the upper base of the cylinder */
        final Vector EXCEPTED_UPPER_NORMAL = new Vector(0,1,0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the cylinder's normal to the expected result when the point is on the envelope.
        /** A point for tests at (5,7,6) */
        final Point P01 = new Point(5,7,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P01), "");
        assertEquals(new Vector(0.8,0,0.6), CYLINDER.getNormal(P01),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC02: Test that compares the cylinder's normal to the expected result when the point is on the lower base.
        /** A point for tests at (2,2,2) */
        final Point P02 = new Point(2,2,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P02), "");
        assertEquals(EXCEPTED_LOWER_NORMAL, CYLINDER.getNormal(P02),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC03: Test that compares the cylinder's normal to the expected result when the point is on the upper base.
        /** A point for tests at (2,12,2) */
        final Point P03 = new Point(2,12,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P03), "");
        assertEquals(EXCEPTED_UPPER_NORMAL, CYLINDER.getNormal(P03),
                "ERROR: The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Checks the cylinder's normal when the point is the center of the lower base.
        /** A point for tests at (1,2,3) */
        final Point P11 = new Point(1,2,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P11), "");
        assertEquals(EXCEPTED_LOWER_NORMAL, CYLINDER.getNormal(P11),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC12: Checks the cylinder's normal when the point is the center of the upper base.
        /** A point for tests at (1,12,3) */
        final Point P12 = new Point(1,12,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P12), "");
        assertEquals(EXCEPTED_UPPER_NORMAL, CYLINDER.getNormal(P12),
                "ERROR: The calculation of the normal isn't as excepted");


        // TC13: Checks the cylinder's normal when the point is between the lower base and the envelope.
        /** A point for tests at (5,2,6) */
        final Point P13 = new Point(5,2,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P13), "");
        assertEquals(EXCEPTED_LOWER_NORMAL, CYLINDER.getNormal(P13),
                "ERROR: The calculation of the normal isn't as excepted");

        // TC14: Checks the cylinder's normal when the point is between the upper base and the envelope.
        /** A point for tests at (5,12,6) */
        final Point P14 = new Point(5,12,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> CYLINDER.getNormal(P14), "");
        assertEquals(EXCEPTED_UPPER_NORMAL, CYLINDER.getNormal(P14),
                "ERROR: The calculation of the normal isn't as excepted");
    }
}