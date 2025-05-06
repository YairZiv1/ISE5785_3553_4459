package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * The Scene class represents a 3D scene with all its components.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;
    /**
     * The background color of the scene, defaults to black.
     */
    public Color background = Color.BLACK;
    /**
     * The ambient light of the scene, defaults to none.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * A collection of geometric objects that compose the scene, defaults to none geometries.
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructs a new scene with a given name.
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     * @param background the background color
     * @return the updated scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     * @param ambientLight the ambient light
     * @return the updated scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     * @param geometries the geometries to set in the scene
     * @return the updated scene
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
