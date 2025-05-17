package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

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
     * List of the light sources of the scene, defaults to empty list, no light sources.
     */
    public List<LightSource> lights =  new LinkedList<>();
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
     * Sets the light sources of the scene.
     * @param lights list of the light sources
     * @return the updated scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
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
