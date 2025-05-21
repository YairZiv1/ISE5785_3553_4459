package primitives;

/**
 * Class Material defines the material of each geometric body.
 * This class if PDS - Passive Data Structure.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Material {
    /**
     * The Ambient light attenuation coefficient, initialized with (1,1,1)
     */
    public Double3 kA = Double3.ONE;
    /**
     * The specular attenuation coefficient, initialized with (0,0,0)
     */
    public Double3 kS = Double3.ZERO;
    /**
     * The diffusive attenuation coefficient, initialized with (0,0,0)
     */
    public Double3 kD = Double3.ZERO;
    /**
     * the material’s shininess, initialized with 0
     */
    public int nSh = 0;

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

    /**
     * Setter for the Ambient light specular attenuation coefficient
     * @param kS the Ambient light specular attenuation coefficient
     * @return the Material
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for the Ambient light specular attenuation coefficient
     * @param kS the Ambient light specular attenuation coefficient
     * @return the Material
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for the Ambient light diffusive attenuation coefficient
     * @param kD the Ambient light diffusive attenuation coefficient
     * @return the Material
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for the Ambient light diffusive attenuation coefficient
     * @param kD the Ambient light diffusive attenuation coefficient
     * @return the Material
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for the material’s shininess
     * @param nSh the material’s shininess
     * @return the Material
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }
}
