package primitives;

/**
 * Class Material define the material of each geometric body.
 * This class if PDS - Passive Data Structure.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Material {
    /**
     * The Ambient light attenuation coefficient, initialized with (1,1,1)
     */
    public Double3 kA = Double3.ONE;

    /**
     * Default constructor
     */
    public Material() {}

    /**
     * Setter for the Ambient light attenuation coefficient
     * @param kA the Ambient light attenuation coefficient
     * @return the Material
     */
    public Material setMaterial(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for the Ambient light attenuation coefficient
     * @param kA the Ambient light attenuation coefficient
     * @return the Material
     */
    public Material setMaterial(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
}
