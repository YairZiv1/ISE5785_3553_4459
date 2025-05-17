package lighting;

import primitives.*;

/**
 * Interface LightSource represents a generic light source in a 3D scene.
 * It defines the basic functionality that all light sources must implement.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public interface LightSource {
    /**
     * Returns the intensity of the light at a given point in space.
     * @param p the point in 3D space where the light intensity is being calculated
     * @return the color representing the light intensity at point
     */
    Color getIntensity(Point p);

    /**
     * Returns a normalized vector from the light source to a given point in space.
     * This vector represents the direction of the incoming light to the point.
     * @param p the point in 3D space for which the direction vector is computed
     * @return the normalized direction vector from the light to point
     */
    Vector getL(Point p);
}
