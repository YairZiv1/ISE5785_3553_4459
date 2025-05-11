package renderer;

import primitives.Ray;
import primitives.Color;
import scene.Scene;

/**
 * RayTracerBase class is an abstract class for ray tracing renderers.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered by the ray tracer.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer with a given scene.
     * @param scene the scene that will be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a given ray through the scene and calculates the color seen along the ray.
     * @param ray the ray from the camera through a pixel
     * @return the color resulting from the ray's interaction with the scene
     */
    public abstract Color traceRay(Ray ray);
}
