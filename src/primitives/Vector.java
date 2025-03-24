package primitives;

/**
 * Class Vector is the basic class representing a Vector of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Vector extends Point {
    /**
     * Constructs a Vector object with the specified x, y, and z.
     * A Vector cannot represent the zero vector, and an exception will be thrown
     * if all components are zero.
     * @param x the x-component of the vector.
     * @param y the y-component of the vector.
     * @param z the z-component of the vector.
     * @throws IllegalArgumentException if all components are zero.
     */
    public Vector(double x, double y, double z) { this(new Double3(x,y,z)); }

    /**
     * Constructs a Vector object using a Double3 object for the coordinates.
     * The constructor check that the Vector does not represent the zero vector
     * (0, 0, 0) and throws an exception if it does.
     * @param d3 the Double3 object representing the components of the vector.
     * @throws IllegalArgumentException if the given coordinates represent the zero vector.
     */
    public Vector(Double3 d3) {
        super(d3);
        if (this.coords.equals(Double3.ZERO))
            throw new IllegalArgumentException("Can't initialize vector 0.");
    }

    /**
     * Sum two Vectors into a new Vector.
     * @param v vector for addition.
     * @return result of add.
     */
    public Vector add(Vector v) {
        return new Vector(this.coords.add(v.coords));
    }

    /**
     * Scale (multiply) triad by a number into a new triad where
     * each number is multiplied by the number.
     * @param rhs right hand side operand for scaling.
     * @return result of scale.
     */
    public Vector scale(double rhs) {
        return new Vector(this.coords.scale(rhs));
    }

    /**
     * Calculates the dot product of this vector with another vector.
     * @param v the vector to calculate the dot product with.
     * @return the dot product of this vector and the specified vector (double).
     */
    public double dotProduct(Vector v) {
        return this.coords.d1() * v.coords.d1()
                + this.coords.d2() * v.coords.d2()
                + this.coords.d3() * v.coords.d3();
    }

    /**
     * Calculates the cross product of this vector with another vector.
     * @param v the vector to calculate the cross product with.
     * @return a new Vector representing the cross product of this vector and the specified vector.
     */
    public Vector crossProduct(Vector v) {
        return new Vector(coords.d2() * v.coords.d3() - coords.d3() * v.coords.d2()
                , coords.d3() * v.coords.d1() - coords.d1() * v.coords.d3()
                , coords.d1() * v.coords.d2() - coords.d2() * v.coords.d1());
    }

    /**
     * Calculates the squared length of the vector.
     * @return the squared length of the vector.
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculates the length of the vector based on its squared length.
     * The length is calculated as the square root of the result that calculated from {@code lengthSquared()}.
     * @return the length of the vector as a double.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes the vector to a unit vector.
     * same vector but with a length of 1.
     * @return a new Vector object representing the normalized (unit) vector.
     */
    public Vector normalize() { return new Vector(coords.reduce(this.length())); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.equals(other);
    }

    @Override
    public String toString() {
        return "Vector: " + coords.toString();
    }
}
