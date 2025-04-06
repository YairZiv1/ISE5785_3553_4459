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
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that subtracts 2 points and compares it to the expected result.
        assertEquals(new Point(1,-1,5), new Point(1, 2, 3).subtract(new Point(0, 3, -2)),
                "ERROR: Point - Point does not work correctly");
    }

    /**
     * Test method for {@link Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that adds 2 points and compares it to the expected result.
        assertEquals(new Point(1,5,1), new Point(1, 2, 3).add(new Vector(0, 3, -2)),
                "ERROR: Point + Vector does not work correctly");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the distance squared between 2 points and compares it to the expected result.
        assertEquals(27, new Point(1, 2, 3).distanceSquared(new Point(0, 3, -2)), DELTA,
                "ERROR: distance-squared between two Points does not work correctly");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the distance between 2 points and compares it to the expected result.
        assertEquals(5, new Point(1, 2, 3).distance(new Point(1, -2, 0)), DELTA,
                "ERROR: distance between two Points does not work correctly");
    }
}