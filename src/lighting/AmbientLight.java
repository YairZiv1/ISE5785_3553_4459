package lighting;

import primitives.Color;

/**
 * AmbientLight class represents a uniform light that affects all objects equally,
 * regardless of their position or other properties.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class AmbientLight {
    /**
     * The color intensity of the ambient light.
     */
    private final Color intensity;

    /**
     * A constant representing the absence of light - black.
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an AmbientLight with the specified color intensity.
     * @param intensity the color and intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the color intensity of the ambient light.
     * @return the ambient light intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
