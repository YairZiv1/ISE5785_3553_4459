package lighting;

import primitives.Color;

/**
 * Class Light is a base class of all types of lights.
 * @author Yair Ziv and Amitay Yosh'i.
 */
abstract class Light {
    /**
     * Variable for the color intensity of light source
     */
    protected final Color intensity;

    /**
     * Constructor fot the intensity
     * @param intensity the intensity value for initialization
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * A getter for the intensity
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return this.intensity;
    }
}
