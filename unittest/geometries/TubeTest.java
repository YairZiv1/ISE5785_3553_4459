package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

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
     * Test method for {@link Tube#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // A tube for test
        final Tube tube = new Tube(1, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray doesn't intersect the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3))),
                "Should be empty list");

        // TC02: Ray starts before and intersects the tube (2 points)
        final var result02 = tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0)));
        assertNotNull(result02, "Can't be empty list");
        assertEquals(2, result02.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-0.857649282009763,-0.514235071799024,1),
                new Point(0.738837400821645, -0.673883740082165, 1)
            ), result02, "Wrong intersection point");

        // TC03: Ray starts inside and intersects the tube (1 point)
        final var result03 = tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1)));
        assertNotNull(result03, "Can't be empty list");
        assertEquals(1, result03.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 1)), result03, "Wrong intersection point");

        // TC04: Ray starts after the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0))),
                "Should be empty list");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray starts at tube
        // TC11: Ray starts at tube and goes inside (1 point)
        final var result11 = tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(1, 1, 0)));
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1)), result11, "Wrong intersection point");

        // TC12: Ray starts at tube and goes outside (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0))),
                "Should be empty list");

        // **** Group 2: Ray, or it's continuation intersects tube through its ray, but not through its head
        // TC21: Ray starts before and not orthogonal to tube's ray (2 points)
        final var result21 = tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1)));
        assertNotNull(result21, "Can't be empty list");
        assertEquals(2, result21.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 1.833333333333333),
                new Point(1.000000000000001, 0, 2.166666666666667)
            ), result21, "Wrong intersection point");

        // TC22: Ray starts before and also orthogonal to tube's ray (2 points)
        final var result22 = tube.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0)));
        assertNotNull(result22, "Can't be empty list");
        assertEquals(2, result22.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 2),
                new Point(1, 0, 2)
            ), result22, "Wrong intersection point");

        // TC23: Ray starts at tube, goes inside, and not orthogonal to tube's ray (1 point)
        final var result23 = tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, 1, 1)));
        assertNotNull(result23, "Can't be empty list");
        assertEquals(1, result23.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 3)), result23, "Wrong intersection point");

        // TC24: Ray starts at tube, goes inside, and also orthogonal to tube's ray (1 point)
        final var result24 = tube.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, 1, 0)));
        assertNotNull(result24, "Can't be empty list");
        assertEquals(1, result24.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 2)), result24, "Wrong intersection point");

        // TC25: Ray starts inside tube and not orthogonal to tube's ray (1 point)
        final var result25 = tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2)));
        assertNotNull(result25, "Can't be empty list");
        assertEquals(1, result25.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 5.999999999999999)), result25, "Wrong intersection point");

        // TC26: Ray starts inside tube and also orthogonal to tube's ray (1 point)
        final var result26 = tube.findIntersections(new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0)));
        assertNotNull(result26, "Can't be empty list");
        assertEquals(1, result26.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 2)), result26, "Wrong intersection point");

        // TC27: Ray starts at tube, goes outside, and not orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, -1, -1))),
                "Should be empty list");

        // TC28: Ray starts at tube, goes outside, and also orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, -1, 0))),
                "Should be empty list");

        // TC29: Ray starts after tube and not orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1))),
                "Should be empty list");

        // TC210: Ray starts after tube and also orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0))),
                "Should be empty list");

        // **** Group 3: Ray, or it's continuation, intersects tube through its head
        // TC31: Ray starts before and not orthogonal to tube's ray (2 points)
        final var result31 = tube.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(6, 0, -1)));
        assertNotNull(result31, "Can't be empty list");
        assertEquals(2, result31.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 1.166666666666667),
                new Point(1.000000000000001, 0, 0.833333333333333)
            ), result31, "Wrong intersection point");

        // TC32: Ray starts before and also orthogonal to tube's ray (2 points)
        final var result32 = tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(6, 0, 0)));
        assertNotNull(result32, "Can't be empty list");
        assertEquals(2, result32.size(), "Wrong number of points");
        assertEquals(List.of(
                new Point(-1, 0, 1),
                new Point(1, 0, 1)
            ), result32, "Wrong intersection point");

        // TC33: Ray starts at tube, goes inside, and not orthogonal to tube's ray (1 point)
        final var result33 = tube.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, 1, -1)));
        assertNotNull(result33, "Can't be empty list");
        assertEquals(1, result33.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 0)), result33, "Wrong intersection point");

        // TC34: Ray starts at tube, goes inside, and also orthogonal to tube's ray (1 point)
        final var result34 = tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, 1, 0)));
        assertNotNull(result34, "Can't be empty list");
        assertEquals(1, result34.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 1, 1)), result34, "Wrong intersection point");

        // TC35: Ray starts inside tube and not orthogonal to tube's ray (1 point)
        final var result35 = tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 1)));
        assertNotNull(result35, "Can't be empty list");
        assertEquals(1, result35.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 3)), result35, "Wrong intersection point");

        // TC36: Ray starts inside tube and also orthogonal to tube's ray (1 point)
        final var result36 = tube.findIntersections(new Ray(new Point(-0.5, 0, 1), new Vector(0.5, 0, 0)));
        assertNotNull(result36, "Can't be empty list");
        assertEquals(1, result36.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1)), result36, "Wrong intersection point");

        // TC37: Ray starts on tube's head and not orthogonal to tube's ray (1 point)
        final var result37 = tube.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 0, 1)));
        assertNotNull(result37, "Can't be empty list");
        assertEquals(1, result37.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 2)), result37, "Wrong intersection point");

        // TC38: Ray starts on tube's head and also orthogonal to tube's ray (1 point)
        final var result38 = tube.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 0, 0)));
        assertNotNull(result38, "Can't be empty list");
        assertEquals(1, result38.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1, 0, 1)), result38, "Wrong intersection point");

        // TC39: Ray starts inside the tube, but after its head, and not orthogonal to tube's ray (1 point)
        final var result39 = tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, -1)));
        assertNotNull(result39, "Can't be empty list");
        assertEquals(1, result39.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, -1)), result39, "Wrong intersection point");

        // TC310: Ray starts inside the tube, but after its head, and also orthogonal to tube's ray (1 point)
        final var result310 = tube.findIntersections(new Ray(new Point(-0.5, 0, 1), new Vector(-0.5, 0, 0)));
        assertNotNull(result310, "Can't be empty list");
        assertEquals(1, result310.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-1, 0, 1)), result310, "Wrong intersection point");

        // TC311: Ray starts at tube, goes outside, and not orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 2), new Vector(0, -1, 1))),
                "Should be empty list");

        // TC312: Ray starts at tube, goes outside, and also orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(0, -1, 0))),
                "Should be empty list");

        // TC313: Ray starts after tube and not orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 1))),
                "Should be empty list");

        // TC314: Ray starts after tube and also orthogonal to tube's ray (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-6, 0, 1), new Vector(-6, 0, 0))),
                "Should be empty list");

        // **** Group 4: Ray is parallel to tube's ray
        // TC41: Ray is inside the tube, and parallels to tube's ray in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC42: Ray is inside the tube, and parallels to tube's ray in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC43: Ray is outside the tube, and parallels to tube's ray in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC44: Ray is outside the tube, and parallels to tube's ray in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC45: Ray is on the tube's surface, and parallels to tube's ray in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC46: Ray is on the tube's surface, and parallels to the tube's ray in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC47: Ray starts before tube's ray and unites in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC48: Ray starts at tube's ray and unites in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC49: Ray starts after tube's ray and unites in the same direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1))),
                "Should be empty list");

        // TC410: Ray starts before tube's ray and unites in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC411: Ray starts at tube's ray and unites in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, -1))),
                "Should be empty list");

        // TC412: Ray starts after tube's ray and unites in the opposite direction (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, -1))),
                "Should be empty list");

        // **** Group 5: Ray is tangent to tube
        // TC51: Ray starts before tube and is tangent to its surface (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(-1, -1, 1), new Vector(1, 0, 0))),
                "Should be empty list");

        // TC52: Ray starts on the tube and is tangent to its surface (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(0, -1, 1), new Vector(1, 0, 0))),
                "Should be empty list");

        // TC53: Ray starts after tube, and its continuation is tangent to its surface (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, -1, 1), new Vector(1, 0, 0))),
                "Should be empty list");
    }

    /**
     * Test method for {@link Tube#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // A tube for test
        final Tube tube = new Tube(3, new Ray(Point.ZERO, Vector.AXIS_Z));
        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the tube
        assertNull(tube.calculateIntersections(new Ray(new Point(-6, 2.5, 0), v100), 3.5),
                "ray stops before the tube");

        // TC02: Ray starts before the tube and "stops" inside it
        final var result02 = tube.calculateIntersections(new Ray(new Point(-4, 1.5, 0), v100), 3.5);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        //TC03: Ray starts and "stops" inside the tube
        assertNull(tube.calculateIntersections(new Ray(new Point(-2, 0.5, 0), v100), 3.5),
                "ray starts and stops inside the tube");

        // TC04: Ray starts inside the tube and "stops" after it
        final var result04 = tube.calculateIntersections(new Ray(new Point(2, -1.5, 0), v100), 3.5);
        assertNotNull(result04, "Can't be empty list");
        assertEquals(1, result04.size(), "Wrong number of points");

        // TC05: Ray starts after the tube
        assertNull(tube.calculateIntersections(new Ray(new Point(4, -2.5, 0), v100), 3.5),
                "ray starts after the tube");

        // TC06: Ray crosses the tube, starts before it and "stops" after it
        final var result06 = tube.calculateIntersections(new Ray(new Point(-4, 1.5, 0), v100), 8);
        assertNotNull(result06, "Can't be empty list");
        assertEquals(2, result06.size(), "Wrong number of points");

        // =============== Boundary Values Tests ==================
        // TC11: Ray starts before the tube and "stops" at the first intersection point
        final var result11 = tube.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 1);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");

        // TC12: Ray starts before the tube and "stops" at the second intersection point
        final var result12 = tube.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 7);
        assertNotNull(result12, "Can't be empty list");
        assertEquals(2, result12.size(), "Wrong number of points");

        // TC13: Ray starts inside the tube and "stops" at the intersection point
        final var result13 = tube.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 5);
        assertNotNull(result13, "Can't be empty list");
        assertEquals(1, result13.size(), "Wrong number of points");
    }
}