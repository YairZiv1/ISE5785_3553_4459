package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Point#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        /** A point for tests to (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests to (0,3,-2) */
        final Point P2 = new Point(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that subtracts 2 points and compares it to the expected result.
        assertEquals(new Point(1,-1,5), P1.subtract(P2),
                "ERROR: Point - Point does not work correctly");
    }

    /**
     * Test method for {@link Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        /** A point for tests to (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A vector for tests to (0,3,-2) */
        final Vector V1 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that adds 2 points and compares it to the expected result.
        assertEquals(new Point(1,5,1), P1.add(V1),
                "ERROR: Point + Vector does not work correctly");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        /** A point for tests to (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests to (0,3,-2) */
        final Point P2 = new Point(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the distance squared between 2 points and compares it to the expected result.
        assertEquals(27, P1.distanceSquared(P2), DELTA,
                "ERROR: distance-squared between two Points does not work correctly");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        /** A point for tests to (1,2,3) */
        final Point P1 = new Point(1, 2, 3);
        /** A point for tests to (1,-2,0) */
        final Point P2 = new Point(1, -2, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the distance between 2 points and compares it to the expected result.
        assertEquals(5, P1.distance(P2), DELTA,
                "ERROR: distance between two Points does not work correctly");
    }
}