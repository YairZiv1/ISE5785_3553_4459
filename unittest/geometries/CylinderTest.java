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
     * Test method for {@link Cylinder#calculateIntersectionsHelper(Ray ray, double maxDistance)}.
     */
    @Test
    void calculateIntersectionsHelper() {
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, -2), new Vector(0, 0, 1)), 10);
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray doesn't intersect the cylinder (0 points)
        Ray ray = new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3));
        var result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // Test Case 02 - Ray starts before and intersects the cylinder (but not through the cylinder's ray) (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        Point intersectionPoint = new Point(-0.857649282009763,-0.514235071799024,1);
        Point intersectionPoint2 = new Point(0.738837400821645, -0.673883740082165, 1);
        var exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // Test Case 03 - Ray starts inside the cylinder (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // Test Case 04 - Ray starts after the cylinder (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray starts at cylinder
        // TC11: Ray starts at cylinder and intersects the cylinder (but not through the cylinder's ray) (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 1, 0));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC12: Ray starts at cylinder and goes outside (continuation does not cross through the cylinder's ray) (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 2: Ray (or it's continuation) intersects cylinder through it's ray (not head)
        // TC21 - Ray starts before and is not orthogonal to cylinder's ray (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1.833333333333333);
        intersectionPoint2 = new Point(1, 0, 2.166666666666667);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC22 - Ray starts before and is orthogonal to cylinder's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 2);
        intersectionPoint2 = new Point(1, 0, 2);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC23: Ray starts at cylinder and is not orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC24: Ray starts at cylinder and is orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, 0));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC25: Ray starts inside the cylinder and is not orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 5.999999999999999);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC26: Ray starts inside cylinder and is orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC27: Ray starts after cylinder and is not orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC28: Ray starts after cylinder and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 3: Ray (or it's continuation) intersects cylinder through it's head
        // TC31 - Ray starts before and is not orthogonal to cylinder's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, -4));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1.000000000000001, 0, -1.333333333333333);
        intersectionPoint2 = new Point(0, 0, -2);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC32 - Ray starts before and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-6, 0, -2), new Vector(6, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC33: Ray starts at cylinder and is not orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, -4));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC34: Ray starts at cylinder and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(0, -1, -2), new Vector(0, 1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC35: Ray starts inside the cylinder and is not orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, -2));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC36: Ray starts inside cylinder and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-0.5, 0, -2), new Vector(0.5, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC37: Ray starts inside the cylinder, but after it's head and is not orthogonal to the cylinder's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 2));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC38: Ray starts inside the cylinder, but after it's head and is orthogonal to the cylinder's ray (0 points)
        ray = new Ray(new Point(-0.5, 0, -2), new Vector(-0.5, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC39: Ray starts at cylinder, but after it's head and is not orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, -1, 4));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-10: Ray starts at cylinder, but after it's head and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(0, -1, -2), new Vector(0, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-11: Ray starts after cylinder and is not orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 4));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-12: Ray starts after cylinder and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(-6, 0, -2), new Vector(-6, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-13: Ray starts on cylinder's head and is not orthogonal to cylinder's ray (1 point)
        ray = new Ray(new Point(0, 0, -2), new Vector(1, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, -1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC3-14: Ray starts on cylinder's head and is orthogonal to cylinder's ray (0 points)
        ray = new Ray(new Point(0, 0, -2), new Vector(1, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-15: Ray starts on the cylinder's top base (1 point)
        ray = new Ray(new Point(0.5, 0.5, 8), new Vector(-0.5, -0.5, -10));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC3-16: Ray starts on the cylinder's bottom base (1 point)
        ray = new Ray(new Point(0, 0, -2), new Vector(-0.5, -0.5, -10));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 4: Ray perpendicular to cylinder's ray
        // TC41: Ray and cylinder's ray unites in the same direction (not through head) (1 point)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, 8);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC42: Ray and cylinder's ray unite in the same direction (through head) (2 points)
        ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        intersectionPoint2 = new Point(0, 0, 8);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC43: Ray and cylinder's ray unite in opposite directions (not through head) (0 points)
        ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC44: Ray and cylinder's ray unites in opposite directions (through head) (1 point)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC45: Ray and cylinder's ray unites in the same direction (starting at head) (1 point)
        ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, 8);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC46: Ray and cylinder's ray unites in opposite directions (starting at head) (0 points)
        ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC47: Ray is an inside cylinder, and it is perpendicular to cylinder's ray in the same direction (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-0.5, 0, 8);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC48: Ray is an inside cylinder, and it is perpendicular to cylinder's ray in opposite directions (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-0.5, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC49: Ray is on the cylinder's side, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-10: Ray is on the cylinder's side, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-11: Ray is outside of cylinder, and it is perpendicular to cylinder's ray in the same direction (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-12: Ray is outside of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-13: Ray is in the center of the top base of the cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(0, 0, 8), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-14: Ray is in the center of the top base of the cylinder, and it is perpendicular to the cylinder's ray in opposite directions (1 point)
        ray = new Ray(new Point(0, 0, 8), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-15: Ray is in the center of the bottom base of the cylinder, and it is perpendicular to cylinder's ray in same direction (1 point)
        ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, 8);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-16: Ray is in the center of the bottom base of the cylinder, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-17: Ray is on the top base of the cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(0.5, 0.5, 8), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-18: Ray is on the top base of the cylinder, and it is perpendicular to the cylinder's ray in opposite directions (1 point)
        ray = new Ray(new Point(0.5, 0.5, 8), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0.5, 0.5, -2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-19: Ray is on the bottom base of the cylinder, and it is perpendicular to cylinder's ray in same direction (1 point)
        ray = new Ray(new Point(0.5, 0.5, -2), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0.5, 0.5, 8);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-20: Ray is on the bottom base of the cylinder, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(0.5, 0.5, -2), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-21: Ray is on the edge of top base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(1, 0, 8), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-22: Ray is on the edge of top base of cylinder, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(1, 0, 8), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-23: Ray is on the edge of bottom base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(1, 0, -2), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-24: Ray is on the edge of bottom base of cylinder, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(1, 0, -2), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-25: Ray starts outside the cylinder against the top base but unites with its ray, and it is perpendicular to cylinder's ray in same direction (0 points)
        ray = new Ray(new Point(0, 0, 9), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-26: Ray starts outside cylinder against top base but unites with its ray, and it is perpendicular to the cylinder's ray in opposite directions (2 points)
        ray = new Ray(new Point(0, 0, 9), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        intersectionPoint2 = new Point(0, 0, 8);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-27: Ray starts outside cylinder against bottom base but unites with its ray, and it is perpendicular to cylinder's ray in same direction (2 points)
        ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 0, -2);
        intersectionPoint2 = new Point(0, 0, 8);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-28: Ray starts outside cylinder against bottom base but unites with its ray, and it is perpendicular to the cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-29: Ray starts outside cylinder against top base, and it is perpendicular to cylinder's ray in the same direction (0 points)
        ray = new Ray(new Point(0.5, 0.5, 9), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-30: Ray starts outside cylinder against top base, and it is perpendicular to cylinder's ray in opposite directions (2 points)
        ray = new Ray(new Point(0.5, 0.5, 9), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0.5, 0.5, -2);
        intersectionPoint2 = new Point(0.5, 0.5, 8);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-31: Ray starts outside cylinder against bottom base, and it is perpendicular to cylinder's ray in the same direction (2 points)
        ray = new Ray(new Point(0.5, 0.5, -3), new Vector(0, 0, 1));
        result = cylinder.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0.5, 0.5, -2);
        intersectionPoint2 = new Point(0.5, 0.5, 8);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC4-32: Ray starts outside cylinder against bottom base, and it is perpendicular to cylinder's ray in opposite directions (0 points)
        ray = new Ray(new Point(0.5, 0.5, -3), new Vector(0, 0, -1));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 5: Ray tangent to cylinder
        // TC51: Ray starts before cylinder and is tangent to it's side (0 points)
        ray = new Ray(new Point(-1, -1, 1), new Vector(1, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC52: Ray starts on the cylinder and is tangent to it's side (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC53: Ray starts after cylinder, and it's continuation is tangent to cylinder's side (0 points)
        ray = new Ray(new Point(1, -1, 1), new Vector(1, 0, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC54: Ray starts before the cylinder and is tangent to the edge between the top base and the cylinder's side (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(5, 0, 7));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC55: Ray starts before the cylinder and is tangent to the edge between the bottom base and the cylinder's side (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(5, 0, -3));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC56: Ray starts on the cylinder and is tangent to the edge between the top base and the cylinder's side (0 points)
        ray = new Ray(new Point(-1, 0, 8), new Vector(5, 0, 7));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC57: Ray starts on the cylinder and is tangent to the edge between the bottom base and the cylinder's side (0 points)
        ray = new Ray(new Point(-1, 0, -2), new Vector(5, 0, -3));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC58: Ray starts after cylinder, and it's continuation is tangent to the edge between the top base and the cylinder's side (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-5, 0, -7));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC59: Ray starts after cylinder, and it's continuation is tangent to the edge between the bottom base and the cylinder's side (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-5, 0, 3));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-10: Ray starts before the cylinder and is tangent to the top base (0 points)
        ray = new Ray(new Point(-6, 0, 8), new Vector(6, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-11: Ray starts before the cylinder and is tangent to the bottom base (0 points)
        ray = new Ray(new Point(-6, 0, -2), new Vector(6, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-12: Ray starts on the cylinder and is tangent to the top base (0 points)
        ray = new Ray(new Point(0, -1, 8), new Vector(6, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-13: Ray starts on the cylinder and is tangent to the bottom base (0 points)
        ray = new Ray(new Point(0, -1, -2), new Vector(6, -1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-14: Ray starts after cylinder and is tangent to the top base (0 points)
        ray = new Ray(new Point(-6, 0, 8), new Vector(-6, 1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC5-15: Ray starts after cylinder and is tangent to the bottom base (0 points)
        ray = new Ray(new Point(-6, 0, -2), new Vector(-6, 1, 0));
        result = cylinder.findIntersections(ray);
        assertNull(result, "The intersections array should be null");
    }
}