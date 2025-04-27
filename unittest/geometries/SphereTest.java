package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Sphere
 *
 * @author Yair Ziv and Amitay Yosh'i
 */
class SphereTest {
    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // A point for tests at (-3,-2,3)
        final Point point = new Point(-3, -2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that compares the triangle's normal to the expected result.
        // A sphere for test
        final Sphere sphere = new Sphere(4, new Point(1, -2, 3));

        // ensure there are no exceptions
        assertDoesNotThrow(() -> sphere.getNormal(point), "");
        assertEquals(new Vector(-1, 0, 0), sphere.getNormal(point),
                "ERROR: The calculation of the normal isn't as excepted");
    }

    /**
     * Test method for {@link Sphere#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // The center point of sphere at (1,0,0)
        final Point  p100 = new Point(1, 0, 0);
        // A sphere for test
        final Sphere sphere = new Sphere(1d, p100);

        // A point used in some test cases
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        // A point used in some test cases
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        // A point used in some test cases
        final Point gp3 = new Point(0.051316701949486, -0.316227766016838, 0);
        // A point used in some test cases
        final Point gp4 = new Point(1.948683298050514, 0.316227766016838, 0);
        // A point used in some test cases at (-1,0,0)
        final Point pm100 = new Point(-1, 0, 0);

        // A vector used in some test cases to (0,0,1)
        final Vector v001 = new Vector(0, 0, 1);
        // A vector used in some test cases to (3,1,0)
        final Vector v310 = new Vector(3, 1, 0);

        // The expected result in some test cases
        final var exp_gp2 = List.of(gp2);
        // The expected result in some test cases
        final var exp_gp4 = List.of(gp4);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pm100, new Vector(1, 1, 0))),
                "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result02 = sphere.findIntersections(new Ray(pm100, v310));
        assertNotNull(result02, "Can't be empty list");
        assertEquals(2, result02.size(), "Wrong number of points");
        assertEquals(List.of(gp1, gp2), result02, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final var result03 = sphere.findIntersections(new Ray(new Point(0.5, 0.5, 0), v310));
        assertNotNull(result03, "Can't be empty list");
        assertEquals(1, result03.size(), "Wrong number of points");
        assertEquals(exp_gp2, result03, "Ray inside sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(2, 1, 0), v310)),
                "Ray starts after sphere");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        final var result11 = sphere.findIntersections(new Ray(gp1, v310));
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(exp_gp2, result11, "Ray at sphere and goes inside not through center");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(gp2, v310)),
                "Ray at sphere and goes outside not through center");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        final var result21 = sphere.findIntersections(new Ray(new Point(-2, -1, 0), v310));
        assertNotNull(result21, "Can't be empty list");
        assertEquals(2, result21.size(), "Wrong number of points");
        assertEquals(List.of(gp3, gp4), result21, "Ray crosses sphere through center");

        // TC22: Ray starts at sphere and goes inside (1 points)
        final var result22 = sphere.findIntersections(new Ray(gp3, v310));
        assertNotNull(result22, "Can't be empty list");
        assertEquals(1, result22.size(), "Wrong number of points");
        assertEquals(exp_gp4, result22, "Ray at sphere and goes inside through center");

        // TC23: Ray starts inside (1 point)
        final var result23 = sphere.findIntersections(new Ray(new Point(1.3, 0.1, 0), v310));
        assertNotNull(result23, "Can't be empty list");
        assertEquals(1, result23.size(), "Wrong number of points");
        assertEquals(exp_gp4, result23, "Ray inside sphere and goes through center");

        // TC24: Ray starts at the center (1 point)
        final var result24 = sphere.findIntersections(new Ray(p100, v310));
        assertNotNull(result24, "Can't be empty list");
        assertEquals(1, result24.size(), "Wrong number of points");
        assertEquals(exp_gp4, result24, "Ray starts at center of sphere");

        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(gp4, v310)),
                "Ray at sphere and goes outside through center");

        // TC26: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(4,1,0), v310)),
                "Ray starts after sphere and goes through center");

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,-1), v001)),
                "Ray starts before tangent point");

        // TC32: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,0), v001)),
                "Ray starts at tangent point");

        // TC33: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,1), v001)),
                "Ray starts after tangent point");

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(3,0,0), v001)),
                "Ray's line outside sphere and orthogonal");

        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        final var result42 = sphere.findIntersections(new Ray(new Point(1.5,0,0), v001));
        assertNotNull(result42, "Can't be empty list");
        assertEquals(1, result42.size(), "Wrong number of points");
        assertEquals(List.of(new Point(1.5, 0, 0.866025403784439)), result42,
                "Ray starts inside sphere and orthogonal");
    }
}