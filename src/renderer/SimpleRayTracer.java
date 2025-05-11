package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * SimpleRayTracer class is a basic implementation of the RayTracerBase.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructs a new SimpleRayTracer with the given scene.
     * @param scene the scene that will be rendered using this ray tracer
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * For now, just returning the intensity of the ambient light
     * @param point not in use for now
     * @return the intensity of the ambient light
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        else {
            Point closestPoint = ray.findClosestPoint(intersections);
            return calcColor(closestPoint);
        }
    }
}
