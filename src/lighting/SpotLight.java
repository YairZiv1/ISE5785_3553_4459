package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * SpotLight represents a point light source with a specific direction and beam concentration.
 * The light is focused along a direction, and intensity can be adjusted using a narrow beam factor.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class SpotLight extends PointLight {
    /**
     * The direction of the spotlight (normalized)
     */
    private final Vector direction;
    /**
     * Beam concentration factor, the larger, the narrower the beam
     */
    private double narrowBeam = 1;

    /**
     * Constructs a spotlight with the given intensity, position, and direction.
     * @param intensity the light intensity
     * @param position  the position of the spotlight
     * @param direction the direction in which the light is focused
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the beam concentration (narrowness).
     * A higher value results in a narrower, more focused beam.
     * @param narrowBeam the narrow beam exponent
     * @return the updated spotlight
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity(p).scale(Math.pow(Math.max(0, direction.dotProduct(getL(p))), narrowBeam));
    }

    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }
}
