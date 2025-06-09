package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Cylinder
 * @author Yair Ziv and Amitay Yosh'i
 */
class CylinderTest {
    /** Default constructor to satisfy JavaDoc generator */
    CylinderTest() { /* to satisfy JavaDoc generator */ }

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
                "The calculation of the normal isn't as excepted");

        // TC02: Test that compares the cylinder's normal to the expected result when the point is on the lower base.
        // A point for tests at (2,2,2)
        final Point p222 = new Point(2,2,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p222), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p222),
                "The calculation of the normal isn't as excepted");

        // TC03: Test that compares the cylinder's normal to the expected result when the point is on the upper base.
        // A point for tests at (2,12,2)
        final Point p2_12_2 = new Point(2,12,2);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p2_12_2), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p2_12_2),
                "The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Checks the cylinder's normal when the point is the center of the lower base.
        // A point for tests at (1,2,3)
        final Point p123 = new Point(1,2,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p123), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p123),
                "The calculation of the normal isn't as excepted");

        // TC12: Checks the cylinder's normal when the point is the center of the upper base.
        // A point for tests at (1,12,3)
        final Point p1_12_3 = new Point(1,12,3);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p1_12_3), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p1_12_3),
                "The calculation of the normal isn't as excepted");


        // TC13: Checks the cylinder's normal when the point is between the lower base and the envelope.
        // A point for tests at (5,2,6)
        final Point p526 = new Point(5,2,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p526), "");
        assertEquals(exceptedLowerNormal, cylinder.getNormal(p526),
                "The calculation of the normal isn't as excepted");

        // TC14: Checks the cylinder's normal when the point is between the upper base and the envelope.
        // A point for tests at (5,12,6)
        final Point p5_12_6 = new Point(5,12,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> cylinder.getNormal(p5_12_6), "");
        assertEquals(exceptedUpperNormal, cylinder.getNormal(p5_12_6),
                "The calculation of the normal isn't as excepted");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // A cylinder for test
        final Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, -2), Vector.AXIS_Z), 10);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray doesn't intersect the cylinder (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3))),
                "Should be empty list");

        // TC02: Ray starts before and intersects the cylinder (2 points)
        final var result02 = cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0)));
        assertNotNull(result02, "Can't be empty list");
        assertEquals(2, result02.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-0.857649282009763,-0.514235071799024,1),
                new Point(0.738837400821645, -0.673883740082165, 1)
        ), result02, "Wrong intersection point");

        // TC03: Ray starts inside and intersects the cylinder (1 point)
        final var result03 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1)));
        assertNotNull(result03, "Can't be empty list");
        assertEquals(1, result03.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 1)), result03, "Wrong intersection point");

        // TC04: Ray starts after the cylinder (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0))),
                "Should be empty list");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray starts at cylinder
        // TC11: Ray starts at cylinder and goes inside (1 point)
        final var result11 = cylinder.findIntersections(new Ray(new Point(0, -1, 1), new Vector(1, 1, 0)));
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1)), result11, "Wrong intersection point");

        // TC12: Ray starts at cylinder and goes outside (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0))),
                "Should be empty list");

        // **** Group 2: Ray, or it's continuation intersects cylinder through its ray, but not through its head
        // TC21: Ray starts before and not orthogonal to cylinder's ray (2 points)
        final var result21 = cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1)));
        assertNotNull(result21, "Can't be empty list");
        assertEquals(2, result21.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 1.833333333333333),
                new Point(1.000000000000001, 0, 2.166666666666667)
        ), result21, "Wrong intersection point");

        // TC22: Ray starts before and also orthogonal to cylinder's ray (2 points)
        final var result22 = cylinder.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0)));
        assertNotNull(result22, "Can't be empty list");
        assertEquals(2, result22.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 2),
                new Point(1, 0, 2)
        ), result22, "Wrong intersection point");

        // TC23: Ray starts at cylinder, goes inside, and not orthogonal to cylinder's ray (1 point)
        final var result23 = cylinder.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, 1, 1)));
        assertNotNull(result23, "Can't be empty list");
        assertEquals(1, result23.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 3)), result23, "Wrong intersection point");

        // TC24: Ray starts at cylinder, goes inside, and also orthogonal to cylinder's ray (1 point)
        final var result24 = cylinder.findIntersections(new Ray(new Point(0, -1, 2), Vector.AXIS_Y));
        assertNotNull(result24, "Can't be empty list");
        assertEquals(1, result24.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 2)), result24, "Wrong intersection point");

        // TC25: Ray starts inside cylinder and not orthogonal to cylinder's ray (1 point)
        final var result25 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2)));
        assertNotNull(result25, "Can't be empty list");
        assertEquals(1, result25.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 5.999999999999999)), result25, "Wrong intersection point");

        // TC26: Ray starts inside cylinder and also orthogonal to cylinder's ray (1 point)
        final var result26 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0)));
        assertNotNull(result26, "Can't be empty list");
        assertEquals(1, result26.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 2)), result26, "Wrong intersection point");

        // TC27: Ray starts at cylinder, goes outside, and not orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, -1, -1))),
                "Should be empty list");

        // TC28: Ray starts at cylinder, goes outside, and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, -1, 0))),
                "Should be empty list");

        // TC29: Ray starts after cylinder and not orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1))),
                "Should be empty list");

        // TC210: Ray starts after cylinder and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0))),
                "Should be empty list");

        // **** Group 3: Ray, or it's continuation, intersects cylinder through its head
        // TC31: Ray starts before and not orthogonal to cylinder's ray (2 points)
        final var result31 = cylinder.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(6, 0, -4)));
        assertNotNull(result31, "Can't be empty list");
        assertEquals(2, result31.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1.000000000000001, 0, -1.333333333333333),
                new Point(0, 0, -2)
        ), result31, "Wrong intersection point");

        // TC32: Ray starts before and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, -2), new Vector(6, 0, 0))),
                "Should be empty list");

        // TC33: Ray starts at cylinder, goes inside, and not orthogonal to cylinder's ray (1 point)
        final var result33 = cylinder.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, 1, -4)));
        assertNotNull(result33, "Can't be empty list");
        assertEquals(1, result33.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, -2)), result33, "Wrong intersection point");

        // TC34: Ray starts at cylinder, goes inside, and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, -2), Vector.AXIS_Y)),
                "Should be empty list");

        // TC35: Ray starts inside cylinder and not orthogonal to cylinder's ray (1 point)
        final var result35 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, -2)));
        assertNotNull(result35, "Can't be empty list");
        assertEquals(1, result35.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, -2)), result35, "Wrong intersection point");

        // TC36: Ray starts inside cylinder and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-0.5, 0, -2), new Vector(0.5, 0, 0))),
                "Should be empty list");

        // TC37: Ray starts on cylinder's head and not orthogonal to cylinder's ray (1 point)
        final var result37 = cylinder.findIntersections(new Ray(new Point(0, 0, -2), new Vector(1, 0, 1)));
        assertNotNull(result37, "Can't be empty list");
        assertEquals(1, result37.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, -1)), result37, "Wrong intersection point");

        // TC38: Ray starts on cylinder's head and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -2), Vector.AXIS_X)),
                "Should be empty list");

        // TC39: Ray starts inside the cylinder, but after its head, and not orthogonal to cylinder's ray (1 point)
        final var result39 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 2)));
        assertNotNull(result39, "Can't be empty list");
        assertEquals(1, result39.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 2)), result39, "Wrong intersection point");

        // TC310: Ray starts inside the cylinder, but after its head, and also orthogonal to the cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-0.5, 0, -2), new Vector(-0.5, 0, 0))),
                "Should be empty list");

        // TC311: Ray starts at cylinder, goes outside, and not orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, -1, 4))),
                "Should be empty list");

        // TC312: Ray starts at cylinder, goes outside, and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, -2), new Vector(0, -1, 0))),
                "Should be empty list");

        // TC313: Ray starts after cylinder and not orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 4))),
                "Should be empty list");

        // TC314: Ray starts after cylinder and also orthogonal to cylinder's ray (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, -2), new Vector(-6, 0, 0))),
                "Should be empty list");

        // TC315: Ray starts on the cylinder's bottom base (1 point)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -2), new Vector(-0.5, -0.5, -10))),
                "Should be empty list");

        // TC316: Ray starts on the cylinder's top base (1 point)
        final var result317 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 8), new Vector(-0.5, -0.5, -10)));
        assertNotNull(result317, "Can't be empty list");
        assertEquals(1, result317.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, -2)), result317, "Wrong intersection point");

        // **** Group 4: Ray parallel to cylinder's ray
        // TC41: Ray and cylinder's ray unites in the same direction (not through head) (1 point)
        final var result41 = cylinder.findIntersections(new Ray(new Point(0, 0, 2), Vector.AXIS_Z));
        assertNotNull(result41, "Can't be empty list");
        assertEquals(1, result41.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, 8)), result41, "Wrong intersection point");

        // TC42: Ray and cylinder's ray unite in the same direction (through head) (2 points)
        final var result42 = cylinder.findIntersections(new Ray(new Point(0, 0, -3), Vector.AXIS_Z));
        assertNotNull(result42, "Can't be empty list");
        assertEquals(2, result42.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(0, 0, -2),
                new Point(0, 0, 8)
            ), result42, "Wrong intersection point");

        // TC43: Ray and cylinder's ray unite in opposite directions (not through head) (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -3), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC44: Ray and cylinder's ray unites in opposite directions (through head) (1 point)
        final var result44 = cylinder.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1)));
        assertNotNull(result44, "Can't be empty list");
        assertEquals(1, result44.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, -2)), result44, "Wrong intersection point");

        // TC45: Ray and cylinder's ray unites in the same direction (starting at head) (1 point)
        final var result45 = cylinder.findIntersections(new Ray(new Point(0, 0, -2), Vector.AXIS_Z));
        assertNotNull(result45, "Can't be empty list");
        assertEquals(1, result45.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, 8)), result45, "Wrong intersection point");

        // TC46: Ray and cylinder's ray unites in opposite directions (starting at head) (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -2), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC47: Ray is an inside cylinder, and it is parallel to cylinder's ray in the same direction (1 point)
        final var result47 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), Vector.AXIS_Z));
        assertNotNull(result47, "Can't be empty list");
        assertEquals(1, result47.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-0.5, 0, 8)), result47, "Wrong intersection point");

        // TC48: Ray is an inside cylinder, and it is parallel to cylinder's ray in opposite directions (1 point)
        final var result48 = cylinder.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1)));
        assertNotNull(result48, "Can't be empty list");
        assertEquals(1, result48.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-0.5, 0, -2)), result48, "Wrong intersection point");

        // TC49: Ray is on the cylinder's surface, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, 0), Vector.AXIS_Z)),
                "Should be empty list");

        // TC410: Ray is on the cylinder's surface, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC411: Ray is outside the cylinder, and it is parallel to cylinder's ray in the same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2, 0, 0), Vector.AXIS_Z)),
                "Should be empty list");

        // TC412: Ray is outside the cylinder, and it is parallel to cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC413: Ray is in the center of the cylinder's top base, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, 8), Vector.AXIS_Z)),
                "Should be empty list");

        // TC414: Ray is in the center of the cylinder's top base, and it is parallel to the cylinder's ray in opposite directions (1 point)
        final var result414 = cylinder.findIntersections(new Ray(new Point(0, 0, 8), new Vector(0, 0, -1)));
        assertNotNull(result414, "Can't be empty list");
        assertEquals(1, result414.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, -2)), result414, "Wrong intersection point");

        // TC415: Ray is in the center of the cylinder's bottom base, and it is parallel to cylinder's ray in same direction (1 point)
        final var result415 = cylinder.findIntersections(new Ray(new Point(0, 0, -2), Vector.AXIS_Z));
        assertNotNull(result415, "Can't be empty list");
        assertEquals(1, result415.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, 8)), result415, "Wrong intersection point");

        // TC416: Ray is in the center of the cylinder's bottom base, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -2), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC417: Ray is on the cylinder's top base, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 8), Vector.AXIS_Z)),
                "Should be empty list");

        // TC418: Ray is on the cylinder's top base, and it is parallel to the cylinder's ray in opposite directions (1 point)
        final var result418 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 8), new Vector(0, 0, -1)));
        assertNotNull(result418, "Can't be empty list");
        assertEquals(1, result418.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.5, 0.5, -2)), result418, "Wrong intersection point");

        // TC419: Ray is on the cylinder's bottom base, and it is parallel to cylinder's ray in same direction (1 point)
        final var result419 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, -2), Vector.AXIS_Z));
        assertNotNull(result419, "Can't be empty list");
        assertEquals(1, result419.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.5, 0.5, 8)), result419, "Wrong intersection point");

        // TC420: Ray is on the cylinder's bottom base, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5, 0.5, -2), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC421: Ray is on the edge of the cylinder's top base, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, 8), Vector.AXIS_Z)),
                "Should be empty list");

        // TC422: Ray is on the edge of the cylinder's top base, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, 8), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC423: Ray is on the edge of the cylinder's bottom base, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, -2), Vector.AXIS_Z)),
                "Should be empty list");

        // TC424: Ray is on the edge of the cylinder's bottom base, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, 0, -2), new Vector(0, 0, -1))),
                "The intersections array");

        // TC425: Ray starts outside the cylinder against the top base but unites with its ray, and it is parallel to cylinder's ray in same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, 9), Vector.AXIS_Z)),
                "Should be empty list");

        // TC426: Ray starts outside the cylinder against top base but unites with its ray, and it is parallel to the cylinder's ray in opposite directions (2 points)
        final var result426 = cylinder.findIntersections(new Ray(new Point(0, 0, 9), new Vector(0, 0, -1)));
        assertNotNull(result426, "Can't be empty list");
        assertEquals(2, result426.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(0, 0, -2),
                new Point(0, 0, 8)
            ), result426, "Wrong intersection point");

        // TC427: Ray starts outside cylinder against bottom base but unites with its ray, and it is parallel to cylinder's ray in same direction (2 points)
        final var result427 = cylinder.findIntersections(new Ray(new Point(0, 0, -3), Vector.AXIS_Z));
        assertNotNull(result427, "Can't be empty list");
        assertEquals(2, result427.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(0, 0, -2),
                new Point(0, 0, 8)
            ), result427, "Wrong intersection point");

        // TC428: Ray starts outside cylinder against bottom base but unites with its ray, and it is parallel to the cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, -3), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC429: Ray starts outside cylinder against top base, and it is parallel to cylinder's ray in the same direction (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 9), Vector.AXIS_Z)),
                "Should be empty list");

        // TC430: Ray starts outside cylinder against top base, and it is parallel to cylinder's ray in opposite directions (2 points)
        final var result430 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 9), new Vector(0, 0, -1)));
        assertNotNull(result430, "Can't be empty list");
        assertEquals(2, result430.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(0.5, 0.5, -2),
                new Point(0.5, 0.5, 8)
            ), result430, "Wrong intersection point");

        // TC431: Ray starts outside cylinder against bottom base, and it is parallel to cylinder's ray in the same direction (2 points)
        final var result431 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, -3), Vector.AXIS_Z));
        assertNotNull(result431, "Can't be empty list");
        assertEquals(2, result431.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(0.5, 0.5, -2),
                new Point(0.5, 0.5, 8)
            ), result431, "Wrong intersection point");

        // TC432: Ray starts outside cylinder against bottom base, and it is parallel to cylinder's ray in opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5, 0.5, -3), new Vector(0, 0, -1))),
                "Should be empty list");

        // **** Group 5: Ray tangent to cylinder
        // TC51: Ray starts before cylinder and is tangent to it's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-1, -1, 1), Vector.AXIS_X)),
                "Should be empty list");

        // TC52: Ray starts on the cylinder and is tangent to it's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 1), Vector.AXIS_X)),
                "Should be empty list");

        // TC53: Ray starts after cylinder, and it's continuation is tangent to the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1, -1, 1), Vector.AXIS_X)),
                "Should be empty list");

        // TC54: Ray starts before the cylinder and is tangent to the edge between the top base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(5, 0, 7))),
                "Should be empty list");

        // TC55: Ray starts before the cylinder and is tangent to the edge between the bottom base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(5, 0, -3))),
                "Should be empty list");

        // TC56: Ray starts on the cylinder and is tangent to the edge between the top base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-1, 0, 8), new Vector(5, 0, 7))),
                "Should be empty list");

        // TC57: Ray starts on the cylinder and is tangent to the edge between the bottom base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-1, 0, -2), new Vector(5, 0, -3))),
                "Should be empty list");

        // TC58: Ray starts after cylinder, and it's continuation is tangent to the edge between the top base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-5, 0, -7))),
                "Should be empty list");

        // TC59: Ray starts after cylinder, and it's continuation is tangent to the edge between the bottom base and the cylinder's surface (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-5, 0, 3))),
                "Should be empty list");

        // TC510: Ray starts before the cylinder and is tangent to the top base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 8), new Vector(6, -1, 0))),
                "Should be empty list");

        // TC511: Ray starts before the cylinder and is tangent to the bottom base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, -2), new Vector(6, -1, 0))),
                "Should be empty list");

        // TC512: Ray starts on the cylinder and is tangent to the top base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, 8), new Vector(6, -1, 0))),
                "Should be empty list");

        // TC513: Ray starts on the cylinder and is tangent to the bottom base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, -1, -2), new Vector(6, -1, 0))),
                "Should be empty list");

        // TC514: Ray starts after cylinder and is tangent to the top base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, 8), new Vector(-6, 1, 0))),
                "Should be empty list");

        // TC515: Ray starts after cylinder and is tangent to the bottom base (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-6, 0, -2), new Vector(-6, 1, 0))),
                "Should be empty list");
    }

    /**
     * Test method for {@link Cylinder#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // A cylinder for test
        final Cylinder cylinder = new Cylinder(3, new Ray(new Point(0, 0, -2), Vector.AXIS_Z), 10);
        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the cylinder
        assertNull(cylinder.calculateIntersections(new Ray(new Point(-6, 2.5, 0), v100), 3.5),
                "ray stops before the cylinder");

        // TC02: Ray starts before the cylinder and "stops" inside it
        final var result02 = cylinder.calculateIntersections(new Ray(new Point(-4, 1.5, 0), v100), 3.5);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        //TC03: Ray starts and "stops" inside the cylinder
        assertNull(cylinder.calculateIntersections(new Ray(new Point(-2, 0.5, 0), v100), 3.5),
                "ray starts and stops inside the cylinder");

        // TC04: Ray starts inside the cylinder and "stops" after it
        final var result04 = cylinder.calculateIntersections(new Ray(new Point(2, -1.5, 0), v100), 3.5);
        assertNotNull(result04, "Can't be empty list");
        assertEquals(1, result04.size(), "Wrong number of points");

        // TC05: Ray starts after the cylinder
        assertNull(cylinder.calculateIntersections(new Ray(new Point(4, -2.5, 0), v100), 3.5),
                "ray starts after the cylinder");

        // TC06: Ray crosses the cylinder, starts before it and "stops" after it
        final var result06 = cylinder.calculateIntersections(new Ray(new Point(-4, 1.5, 0), v100), 8);
        assertNotNull(result06, "Can't be empty list");
        assertEquals(2, result06.size(), "Wrong number of points");

        // =============== Boundary Values Tests ==================
        // TC11: Ray starts before the cylinder and "stops" at the first intersection point
        final var result11 = cylinder.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 1);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");

        // TC12: Ray starts before the cylinder and "stops" at the second intersection point
        final var result12 = cylinder.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 7);
        assertNotNull(result12, "Can't be empty list");
        assertEquals(2, result12.size(), "Wrong number of points");

        // TC13: Ray starts inside the cylinder and "stops" at the intersection point
        final var result13 = cylinder.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 5);
        assertNotNull(result13, "Can't be empty list");
        assertEquals(1, result13.size(), "Wrong number of points");
    }
}