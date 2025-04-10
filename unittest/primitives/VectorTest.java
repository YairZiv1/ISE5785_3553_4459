package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Vector
 * @author Yair Ziv and Amitay Yosh'i
 */
class VectorTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link Vector#Vector(double, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check for a vector represented by 3 doubles.
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "ERROR: Vector constructor does not work correctly");

        // TC02: Check for a vector represented by Double3.
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)),
                "ERROR: Vector constructor does not work correctly");

        // =============== Boundary Values Tests ==================

        // TC10: If the given coordinates represent the zero vector (3 doubles).
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0),
                "ERROR: Vector can not be the zero vector");

        // TC11: If the given coordinates represent the zero vector. (Double3)
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO),
                "ERROR: Vector can not be the zero vector");
    }

    /**
     * Test method for {@link Vector#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that subtracts 2 vectors and compares it to the expected result.
        assertEquals(new Vector(1,-1,5), V1.subtract(new Vector(0, 3, -2)),
                "ERROR: Vector - Vector does not work correctly");

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

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that adds 2 vectors and compares it to the expected result.
        assertEquals(new Vector(1,5,1), V1.add(new Vector(0, 3, -2)),
                "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that adds a vector to its opposite and check it throws an exception.
        assertThrows(IllegalArgumentException.class, () -> V1.add(new Vector(-1, -2, -3)),
                "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    void testScale() {
        /** A vector for tests to (1,-2,3) */
        final Vector V1 = new Vector(1, -2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that scales a vector and compares it to the expected result.
        assertEquals(new Vector(2,-4,6), V1.scale(2),
                "ERROR: Vector * Scalar does not work correctly");

        // TC02: Test that scales a vector with a negative scalar and compares it to the expected result.
        assertEquals(new Vector(-2,4,-6), V1.scale(-2),
                "ERROR: Vector * negative Scalar does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that scales a vector with scalar zero and check it throws an exception.
        assertThrows(IllegalArgumentException.class, () -> V1.scale(0),
                "ERROR: Vector * 0 does not throw an exception");
    }

    /**
     * Test method for {@link Vector#dotProduct{Vector})}.
     */
    @Test
    void testDotProduct() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that do a dot-product between 2 vectors with an acute angle
        // and compares it to the expected result.
        assertEquals(12, V1.dotProduct(new Vector(2, 2, 2)),
                "ERROR: Vector * Vector does not work correctly for angle 0 < a < 90");

        // TC02: Test that do a dot-product between 2 vectors with an obtuse angle
        // and compares it to the expected result.
        assertEquals(-3, V1.dotProduct(new Vector(-4, -1, 1)),
                "ERROR: Vector * Vector does not work correctly for angle 90 < a < 180");

        // =============== Boundary Values Tests ==================
        // TC11: Test that do a dot-product between 2 parallel vectors and compares it to the expected result.
        assertEquals(28, V1.dotProduct(new Vector(2, 4, 6)),
                "ERROR: Vector * parallel-Vector does not work correctly");

        // TC12: Test that do a dot-product between 2 opposite vectors and compares it to the expected result.
        assertEquals(-28, V1.dotProduct(new Vector(-2, -4, -6)),
                "ERROR: Vector * opposite-Vector does not work correctly");

        // TC13: Test that do a dot-product between a vector and its orthogonal (90 degrees)
        // and compares it to the expected result.
        assertEquals(0, V1.dotProduct(new Vector(3, -3, 1)), DELTA,
                "ERROR: Vector * its-orthogonal (90 degrees) does not work correctly");

        // TC14: Test that do a dot-product between a vector and its orthogonal (270 degrees)
        // and compares it to the expected result.
        assertEquals(0, V1.dotProduct(new Vector(-3, 3, -1)), DELTA,
                "ERROR: Vector * its-orthogonal (270 degrees) does not work correctly");
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        /** A vector for tests to (1,2,3) */
        final Vector V1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that do a cross-product between to vectors and compares it to the expected result.
        assertEquals(new Vector(-13,2,3), V1.crossProduct(new Vector(0, 3, -2)),
                "ERROR: Vector X Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: Test that do a cross-product between the vector and itself and compares it to the expected result.
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(V1),
                "ERROR: Vector X itself does not throw an exception");

        // TC12: Test that do a cross-product between the vector and its parallel
        // and compares it to the expected result.
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(new Vector(2, 4, 6)),
                "ERROR: Vector X parallel-Vector does not throw an exception");

        // TC13: Test that do a cross-product between the vector and its opposite
        // and compares it to the expected result.
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(new Vector(-2, -4, -6)),
                "ERROR: Vector X opposite-Vector does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the length squared of a vector and compares it to the expected result.
        // (without negative)
        assertEquals(14, new Vector(1, 2, 3).lengthSquared(), DELTA,
                "ERROR: Vector length squared does not work correctly");

        // TC02: Test that calculates the length squared of a vector and compares it to the expected result.
        // (with negative)
        assertEquals(14, new Vector(-1, -2, 3).lengthSquared(), DELTA,
                "ERROR: Vector length squared does not work correctly");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that calculates the length of a vector and compares it to the expected result.
        // (without negative)
        assertEquals(5, new Vector(0, 3, 4).length(), DELTA,
                "ERROR: Vector length does not work correctly");

        // TC02: Test that calculates the length of a vector and compares it to the expected result.
        // (with negative)
        assertEquals(5, new Vector(0, -3, -4).length(), DELTA,
                "ERROR: Vector length does not work correctly");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that normalizes a vector and compares it to the expected result.
        assertEquals(new Vector(0, 0.6, 0.8), new Vector(0, 3, 4).normalize(),
                "ERROR: Vector normalize does not work correctly");
    }
}