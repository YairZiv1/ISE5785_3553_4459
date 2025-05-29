package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Tube
 * @author Yair Ziv and Amitay Yosh'i
 */
class TubeTest {
    /** Default constructor to satisfy JavaDoc generator */
    TubeTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // A tube for test
        final Tube tube = new Tube(5, new Ray(new Point(1,2,3), new Vector(0,1,0)));
        // A vector for the excepted normal
        final Vector exceptedNormal = new Vector(0.8,0,0.6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the tube's normal to the expected result.
        // A point for tests at (5,7,6)
        final Point p576 = new Point(5,7,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> tube.getNormal(p576), "");
        assertEquals(exceptedNormal, tube.getNormal(p576),
                "The calculation of the normal isn't as excepted");

        // =============== Boundary Values Tests ==================
        // TC11: Test that compares the tube's normal to the expected result
        // when the point is opposite the head of the ray (Point that closest to the head).
        // A point for tests at (5,2,6)
        final Point p526 = new Point(5,2,6);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> tube.getNormal(p526), "");
        assertEquals(exceptedNormal, tube.getNormal(p526),
                "The calculation of the normal isn't as excepted");
    }

    /**
     * Test method for {@link Tube#calculateIntersectionsHelper(Ray ray, double maxDistance)}.
     */
    @Test
    void calculateIntersectionsHelper() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray doesn't intersect the tube (0 points)
        Ray ray = new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3));
        var result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // Test Case 02 - Ray starts before and intersects the tube (but not through the tube's ray) (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        Point intersectionPoint = new Point(-0.857649282009763,-0.514235071799024,1);
        Point intersectionPoint2 = new Point(0.738837400821645, -0.673883740082165, 1);
        var exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // Test Case 03 - Ray starts inside the tube (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // Test Case 04 - Ray starts after the tube (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");



        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray starts at tube
        // TC11: Ray starts at tube and intersects the tube (but not through the tube's ray) (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC12: Ray starts at tube and goes outside (continuation does not cross through the tube's ray) (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 2: Ray (or it's continuation) intersects tube through it's ray (not head)
        // TC21 - Ray starts before and is not orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1.833333333333333);
        intersectionPoint2 = new Point(1.000000000000001, 0, 2.166666666666667);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC22 - Ray starts before and is orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 2);
        intersectionPoint2 = new Point(1, 0, 2);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC23: Ray starts at tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC24: Ray starts at tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC25: Ray starts inside tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 5.999999999999999);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC26: Ray starts inside tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC27: Ray starts after tube and is not orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC28: Ray starts after tube and is orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 3: Ray (or it's continuation) intersects tube through it's head
        // TC31 - Ray starts before and is not orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1.166666666666667);
        intersectionPoint2 = new Point(1.000000000000001, 0, 0.833333333333333);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC32 - Ray starts before and is orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(2, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        intersectionPoint2 = new Point(1, 0, 1);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "Wrong intersection point");

        // TC33: Ray starts at tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 0);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC34: Ray starts at tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC35: Ray starts inside tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC36: Ray starts inside tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 1), new Vector(0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC37: Ray starts inside the tube, but after it's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, -1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC38: Ray starts inside the tube, but after it's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 1), new Vector(-0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC39: Ray starts at tube, but after it's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, -1, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-10: Ray starts at tube, but after it's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, -1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-11: Ray starts after tube and is not orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-12: Ray starts after tube and is orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC3-13: Ray starts on tube's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");

        // TC3-14: Ray starts on tube's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "The intersections array should not be null");
        assertEquals(1, result.size(), "Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "Wrong intersection point");



        // **** Group 4: Ray perpendicular to tube's ray
        // TC41: Ray and tube's ray unites in the same direction (not through head) (0 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC42: Ray and tube's ray unite in the same direction (through head) (0 points)
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC43: Ray and tube's ray unite in opposite directions (not through head) (0 points)
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC44: Ray and tube's ray unite in opposite directions (through head) (0 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC45: Ray and tube's ray unite in the same direction (starting at head) (0 points)
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC46: Ray and tube's ray unite in opposite directions (starting at head) (0 points)
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC47: Ray is inside tube, and it is perpendicular to tube's ray in the same direction (0 points)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC48: Ray is inside tube, and it is perpendicular to tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC49: Ray is on the tube's side, and it is perpendicular to tube's ray in the same direction (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-10: Ray is on the tube's side, and it is perpendicular to the tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-11: Ray is outside of tube, and it is perpendicular to tube's ray in the same direction (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC4-12: Ray is outside of tube, and it is perpendicular to tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");


        // **** Group 5: Ray tangent to tube
        // TC51: Ray starts before tube and is tangent to it's side (0 points)
        ray = new Ray(new Point(-1, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC52: Ray starts on the tube and is tangent to it's side (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");

        // TC53: Ray starts after tube, and it's continuation is tangent to tube's side (0 points)
        ray = new Ray(new Point(1, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "The intersections array should be null");
    }
}