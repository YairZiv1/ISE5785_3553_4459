package geometries;

import primitives.*;

/**
 * Class Geometry is the basic interface representing a general geometric shape
 * of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system
 * This class if PDS - Passive Data Structure.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public abstract class Geometry extends Intersectable {
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
}
