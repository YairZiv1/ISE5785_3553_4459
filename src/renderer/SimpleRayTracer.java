package renderer;

import geometries.Intersectable. Intersection;
import primitives.Color;
import primitives.Double3;
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
     * Calculates the color of an intersection
     * @param intersection the intersection for calculating the color
     * @return the intensity of the ambient light plus the emission of the intersection
     */
    private Color calcColor(Intersection intersection) {
        Color ambientLightIntensity = scene.ambientLight.getIntensity();
        Double3 attenuationCoefficient = intersection.geometry.getMaterial().kA;

        Color intensity = ambientLightIntensity.scale(attenuationCoefficient);
        Color emission = intersection.geometry.getEmission();
        return intensity.add(emission);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        if (intersections == null)
            return scene.background;
        else {
            Intersection closestPoint = ray.findClosestIntersection(intersections);
            return calcColor(closestPoint);
        }
    }
}
