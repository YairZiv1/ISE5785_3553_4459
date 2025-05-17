package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * PointLight represents a light source that emits light from a single point in space,
 * with intensity decreasing based on distance only using attenuation factors.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the light source
     */
    private final Point position;
    /**
     * Constant attenuation factor
     */
    private double kC = 1;
    /**
     * Linear attenuation factor
     */
    private double kL = 0;
    /**
     * Quadratic attenuation factor
     */
    private double kQ = 0;

    /**
     * Constructs a point light source with the specified intensity and position.
     * @param intensity the initial intensity of the light
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        // calculates the distance squared so that we won't calculate its root and then square it back
        double distanceSquared = position.distanceSquared(p);
        // reduce is for integers only
        // in java, (n / d) in  floats, returns infinity when d equals to zero, so no check needed
        return intensity.scale(1 / (kC + kL * Math.sqrt(distanceSquared) + kQ * distanceSquared));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    /**
     * Sets the constant attenuation factor.
     * @param kC the constant attenuation coefficient
     * @return the updated point light
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     * @param kL the linear attenuation coefficient
     * @return the updated point light
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     * @param kQ the quadratic attenuation coefficient
     * @return the updated point light
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }
}
