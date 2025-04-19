package geometries;

/**
 * Class RadialGeometry is the basic abstract class representing a geometric shapes that have a radius
 * of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i
 */
public abstract class RadialGeometry implements Geometry {
    /**
     * Represents the radius of a radial geometry object.
     * This field is immutable and must be provided during object construction
     */
    protected final double radius;

    /**
     * Constructs a new RadialGeometry object with the specified radius.
     * @param radius the radius of the radial geometry; must be a positive value
     * @throws IllegalArgumentException if the given radius is negative.
     */
    public RadialGeometry(double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("Can't initialize negative radius.");
        this.radius = radius;
    }
}
