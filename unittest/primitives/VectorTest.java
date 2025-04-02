package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Vector#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);
        /** A vector for tests to (0,3,-2) */
        final Vector V2 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that subtracts 2 vectors and compares it to the expected result.
        assertEquals(new Vector(1,-1,5), V1.subtract(V2), "ERROR: Vector - Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that subtracts a vector to itself and check it throws an exception.
        assertThrows(IllegalArgumentException.class, () -> V1.subtract(V1),
                "ERROR: Vector - itself does not throw an exception");
    }

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);
        /** A vector for tests to (-1,-2,-3) (opposite to V1) */
        final Vector V1_OPPOSITE = new Vector(-1, -2, -3);
        /** A vector for tests to (0,3,-2) */
        final Vector V2 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that adds 2 vectors and compares it to the expected result.
        assertEquals(new Vector(1,5,1), V1.add(V2), "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that adds a vector to its opposite and check it throws an exception.
        assertThrows(IllegalArgumentException.class, () -> V1.add(V1_OPPOSITE),
                "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    void testScale() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);
        /** Scalar 2 */
        final double SCALAR = 2;
        /** Scalar 0 */
        final double SCALAR_ZERO = 0;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that scales a vector and compares it to the expected result.
        assertEquals(new Vector(2,4,6), V1.scale(SCALAR), "ERROR: Vector * Scalar does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that scales a vector with scalar zero and check it throws an exception.
        assertThrows(IllegalArgumentException.class, () -> V1.scale(SCALAR_ZERO),
                "ERROR: Vector * 0 does not throw an exception");
    }

    /**
     * Test method for {@link Vector#dotProduct{Vector})}.
     */
    @Test
    void testDotProduct() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);
        /** A vector for tests to (0,3,-2) */
        final Vector V2 = new Vector(0, 4, -2);
        /** A vector for tests to (0,3,-2) */
        final Vector V1_ORTHOGONAL = new Vector(3, -3, 1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that do a dot-product between 2 vectors and compares it to the expected result.
        assertEquals(2, V1.dotProduct(V2), "ERROR: Vector * Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that do a dot-product between a vector and its orthogonal and compares it to the expected result.
        assertEquals(0, V1.dotProduct(V1_ORTHOGONAL), DELTA,
                "ERROR: Vector * its-orthogonal does not work correctly");
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);
        /** A vector for tests to (0,3,-2) */
        final Vector V2 = new Vector(0, 3, -2);
        /** A vector for tests to (2,4,6) */
        final Vector V1_PARALLEL = new Vector(2, 4, 6);


        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that do a cross-product between to vectors and compares it to the expected result.
        assertEquals(new Vector(-13,2,3), V1.crossProduct(V2),
                "ERROR: Vector X Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that do a cross-product between to vector and its-parallel and compares it to the expected result.
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(V1_PARALLEL), //
                "ERROR: Vector X parallel-Vector does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the length squared of a vector and compares it to the expected result.
        assertEquals(14, V1.lengthSquared(), DELTA,
                "ERROR: Vector length squared does not work correctly");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        /** A vector for tests to (0,3,4) */
        final Vector V1 = new Vector(0, 3, 4);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the length of a vector and compares it to the expected result.
        assertEquals(5, V1.length(), DELTA,
                "ERROR: Vector length does not work correctly");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        /** A vector for tests to (0,3,4) */
        final Vector V1 = new Vector(0, 3, 4);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that normalizes a vector and compares it to the expected result.
        assertEquals(new Vector(0, 0.6, 0.8), V1.normalize(),
                "ERROR: Vector normalize does not work correctly");
    }
}