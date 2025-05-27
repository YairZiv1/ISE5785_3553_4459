package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

import java.util.List;

/**
 * Testing Polygons
 * @author Dan.
 */
class PolygonTest {
    /** Default constructor to satisfy JavaDoc generator */
    PolygonTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals.
     */
    private static final double DELTA = 0.000001;

    /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertices on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertices on a side");

    }

    /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link Polygon#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // A polygon for test
        final Polygon polygon = new Polygon(
                new Point(0,0,1), new Point(-1,0,0), new Point(-1,1,0));

        // A vector used in some test cases to (0,0,1)
        final Vector v001 = new Vector(0,0,1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is inside the polygon (1 point)
        final var result01 = polygon.findIntersections(new Ray(new Point(-0.7,0.5,0), v001));
        assertNotNull(result01, "Can't be empty list");
        assertEquals(1, result01.size(), "Wrong number of points");
        assertEquals(List.of(new Point(-0.7,0.5,0.3)), result01, "Ray inside polygon");

        // TC02: Ray is outside the polygon against edge (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(-2,0.5,-2), v001)),
                "Ray outside polygon against edge");

        // TC03: Ray is outside the polygon against vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(-2,3,-2), v001)),
                "Ray outside polygon against vertex");

        // =============== Boundary Values Tests ==================
        // TC11: Ray is on the edge
        assertNull(polygon.findIntersections(new Ray(new Point(-0.5,0.5,0), v001)),
                "Ray on edge");

        // TC12: Ray is on the vertex
        assertNull(polygon.findIntersections(new Ray(new Point(0,0,0.5), v001)),
                "Ray on vertex");

        // TC13: Ray is on the edge's continuation
        assertNull(polygon.findIntersections(new Ray(new Point(-2,2,-2), v001)),
                "Ray on edge's continuation");
    }

    /**
     * Test method for {@link Polygon#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // A point for tests at (0,-1,1)
        final Point p0m11 = new Point(0, -1, 1);
        // A point for tests at (0,-1,-1)
        final Point p0m1m1 = new Point(0, -1, -1);
        // A point for tests at (0,1,1)
        final Point p011 = new Point(0, 1, 1);
        // A point for tests at (0,1,-1)
        final Point p01m1 = new Point(0, 1, -1);
        // A plane for test
        final Polygon polygon = new Polygon(p0m11, p0m1m1, p01m1, p011);

        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray "stops" before the polygon
        assertNull(polygon.calculateIntersections(new Ray(new Point(-3, 0, 0), v100), 2),
                "ray stops before the polygon");

        // TC02: Ray crosses the polygon
        final var result02 = polygon.calculateIntersections(new Ray(new Point(-1, 0, 0), v100), 2);
        assertNotNull(result02, "Can't be empty list");
        assertEquals(1, result02.size(), "Wrong number of points");

        // TC03: Ray starts after the polygon
        assertNull(polygon.calculateIntersections(new Ray(new Point(1, 0, 0), v100), 2),
                "ray starts after the polygon");

        // =============== Boundary Values Tests ==================
        // TC11: Ray "stops" at the polygon
        final var result11 = polygon.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 2);
        assertNotNull(result11, "Can't be empty list");
        assertEquals(1, result11.size(), "Wrong number of points");
    }
}