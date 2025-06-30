package geometries;

import primitives.*;

/**
 * Class Geometry is the basic interface representing a general geometric shape
 * of Euclidean geometry in a Cartesian
 * 3-Dimensional coordinate system.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public abstract class Geometry extends Intersectable {
    /** Default constructor to satisfy JavaDoc generator */
    Geometry() { /* to satisfy JavaDoc generator */ }

    /**
     * The shining color
     */
    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry
     */
    private Material material = new Material();

    /**
     * Calculates the normal vector to the geometric object at the given point.
     * @param p the point on the surface of the geometric object where the normal is to be calculated
     * @return the normal vector to the geometric object at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     * Getter for emission
     * @return the emission
     */
    public Color getEmission() {
        return this.emission;
    }

    /**
     * Set the emission color of the geometry
     * @param emission the emission color to set
     * @return the geometry
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Getter method for the material of the geometry
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Setter method for the material of the geometry
     * @param material the material of the geometry
     * @return the geometry
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    @Override
    public Intersectable move(Vector offset) {
        return this.moveHelper(offset)
                .setEmission(this.emission)
                .setMaterial(this.material);
    }

    /**
     * Helper method to move the geometry by a given offset.
     * @param offset the vector by which to move the geometry
     * @return a new Geometry object moved by the offset without emission and material
     */
    protected abstract Geometry moveHelper(Vector offset);
}
