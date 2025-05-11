package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Geometries
 * @author Yair Ziv and Amitay Yosh'i
 */
class GeometriesTest {
    /**
     * * Test method for {@link Geometries#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // The axis vector of ray to (0,0,1)
        final Vector v001 = new Vector(0,0,1);
        // A ray for test
        final Ray ray = new Ray(new Point(1,1,1),v001);

        // A point used in some test cases at (-1,0,0)
        final Point pm100 = new Point(-1,0,0);
        // A point used in some test cases at (0,2,1)
        final Point p021 = new Point(0,2,1);
        // A point used in some test cases at (2,2,1)
        final Point p221 = new Point(2,2,1);
        // A point used in some test cases at (0,-1,3)
        final Point p0m13 = new Point(0,-1,3);
        // A point used in some test cases at (-1,3,3)
        final Point pm133 = new Point(-1,3,3);

        // A plane used in some test cases - 1 intersections with ray
        final Plane plane1 = new Plane(pm133, v001);
        // A plane used in some test cases - 0 intersections with ray
        final Plane plane2 = new Plane(pm100, v001);
        // A polygon used in some test cases - 1 intersections with ray
        final Polygon polygon1 = new Polygon(p021, p221, new Point(2,-1,3), p0m13);
        // A polygon used in some test cases - 0 intersections with ray
        final Polygon polygon2 = new Polygon(p021, p221, new Point(1,3,3), pm133);
        // A triangle used in some test cases - 1 intersections with ray
        final Triangle triangle1 = new Triangle(p021, p221, p0m13);
        // A triangle used in some test cases - 0 intersections with ray
        final Triangle triangle2 = new Triangle(p021, p221, pm133);
        // A sphere used in some test cases - 2 intersections with ray
        final Sphere sphere1 = new Sphere(new Point(1,1,4), 1);
        // A sphere used in some test cases - 0 intersections with ray
        final Sphere sphere2 = new Sphere(pm100, 1);

        // geometries that we didn't implement their findIntersections method yet:
        // A tube used in some test cases - 1 intersections with ray
        // final Tube tube1 = new Tube(2, new Ray(new Point(1,1,0),new Vector(0,1,0)));
        // A tube used in some test cases - 0 intersections with ray
        // final Tube tube2 = new Tube(1, new Ray(new Point(-1,0,0),v001));
        // A cylinder used in some test cases - 2 intersections with ray
        // final Cylinder cylinder1 = new Cylinder(2, new Ray(new Point(1,1,6),v001), 6);
        // A cylinder used in some test cases - 0 intersections with ray
        // final Cylinder cylinder2 = new Cylinder(2, new Ray(new Point(1,1,-6),v001), 6);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some geometries have intersections with ray and some not
        final var result01 = new Geometries
                (plane1, polygon2, triangle1, sphere2).findIntersections(ray);
        assertNotNull(result01, "Can't be empty list");
        assertEquals(2, result01.size(), "Wrong number of points");

        // =============== Boundary Values Tests ==================
        // TC11: There are no geometries at all
        assertNull(new Geometries().findIntersections(ray),
                "No geometries");

        // TC12: All geometries don't have intersections with ray
        assertNull(new Geometries(plane2, polygon2, triangle2, sphere2).findIntersections(ray),
                "no intersections");

        // TC13: Only one geometry has intersections with ray
        final var result13 = new Geometries
                (plane1, polygon2, triangle2, sphere2).findIntersections(ray);
        assertNotNull(result13, "Can't be empty list");
        assertEquals(1, result13.size(), "Wrong number of points");

        // TC14: All geometries have intersections with ray
        final var result14 = new Geometries
                (plane1, polygon1, triangle1, sphere1).findIntersections(ray);
        assertNotNull(result14, "Can't be empty list");
        assertEquals(5, result14.size(), "Wrong number of points");
    }
}