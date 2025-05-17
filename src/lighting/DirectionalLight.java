package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * DirectionalLight represents a light source with parallel rays coming from a very far away place in some direction,
 * similar to sunlight. The light has constant intensity everywhere.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction vector of the light rays (normalized)
     */
    private final Vector direction;

    /**
     * Constructs a directional light source with the specified intensity and direction.
     * @param intensity the color intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }
}
