package renderer;

import geometries.Intersectable. Intersection;
import lighting.LightSource;
import primitives.*;
import sceneTest.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * SimpleRayTracer class is a basic implementation of the RayTracerBase.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Maximum recursion depth for calculating global lighting effects (reflection and refraction).
     * This prevents infinite loops and excessive calculation when tracing reflection/refraction rays.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum attenuation factor (k) for continuing recursive global lighting calculations.
     * When the accumulated transparency or reflection coefficient drops below this threshold,
     * the change is going to be minor, so the recursion stops.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial attenuation factor used when starting the global lighting calculation.
     * Represents full intensity (no attenuation) and is scaled down during recursive tracing
     * by transparency and reflection coefficients.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructs a new SimpleRayTracer with the given scene.
     * @param scene the scene that will be rendered using this ray tracer
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Checks if the intersection point is unshaded by any geometry.
     * @param intersection the intersection point to check
     * @return true if the intersection point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1); // from the point to the light source

        // Create a ray from the point to the light source
        Ray ray = new Ray(intersection.point, pointToLight, intersection.normal);

        var intersections = scene.geometries.
                calculateIntersections(ray, intersection.light.getDistance(intersection.point));

        if (intersections == null)
            return true;
        else {
            for (Intersection i : intersections)
                if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                    return false;
        }
        return true;
    }

    /**
     * Calculates the transparency factor (ktr) from the intersection point toward the light source.
     * @param intersection the intersection point being evaluated
     * @return the accumulated transparency factor along the path to the light source
     */
    private Double3 transparency(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1); // from the point to the light source

        Ray ray = new Ray(intersection.point, pointToLight, intersection.normal); // create a ray from the point to the light source
        var intersections = scene.geometries.calculateIntersections(ray, intersection.light.getDistance(intersection.point));

        Double3 ktr = Double3.ONE;

        if (intersections == null)
            return ktr;
        else {
            for (Intersection i : intersections) {
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
                ktr = ktr.product(i.material.kT);
            }
        }
        return ktr;
    }

    /**
     * Initializes the fields of the ray direction vector, the normal vector, and their dot product,
     * and returns false if the dot product is equal to zero, and true otherwise
     * @param intersection the intersection to update
     * @param rayDirection the ray direction vector
     * @return false if the dot product is equal to zero, and true otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);

        return !isZero(intersection.vNormal);
    }

    /**
     * Initializes the fields related to the light source at intersection,
     * and returns false if both dot products are equal to zero, and true otherwise
     * @param intersection the intersection to update
     * @param light the source light
     * @return false if both dot products are equal to zero, and true otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource light) {
        // Assumes that preprocessIntersection has already been executed; otherwise there may be
        // problems running the code. But it is only called internally, so there are no checks.
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);

        return alignZero(intersection.lNormal * intersection.vNormal) > 0;
    }

    /**
     * Calculates the global lighting effects (reflection and refraction) at the intersection point.
     * @param intersection the intersection point being evaluated
     * @param level the recursion level for global lighting
     * @param k the accumulated transparency/reflection coefficient
     * @return the resulting color from global lighting effects
     */
    private Color calcColorGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color globalEffectColor1 = calcColorGlobalEffect(constructRefractedRay(intersection),
                level, k, intersection.material.kT);

        Color globalEffectColor2 = calcColorGlobalEffect(constructReflectedRay(intersection),
                level, k, intersection.material.kR);

        return globalEffectColor1.add(globalEffectColor2);
    }

    /**
     * Calculates the global color contribution from a single reflected or refracted ray.
     * @param ray the reflected or refracted ray
     * @param level the current recursion level
     * @param k the accumulated attenuation factor so far
     * @param kx the reflection or refraction coefficient for the current step
     * @return the color contribution from this global effect
     */
    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);

        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);

        if (intersection == null)
            return scene.background.scale(kx);

        if (preprocessIntersection(intersection, ray.getVector()))
            return calcColor(intersection, level - 1, kkx).scale(kx);
        else
            return Color.BLACK;
    }

    /**
     * Calculates the refracted ray starting from the intersection point, with the vector of the intersecting ray.
     * @param intersection the intersection point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Calculates the reflected ray starting from the intersection point.
     * @param intersection the intersection point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        // According to the formula
        Vector r = intersection.v.add((intersection.normal.scale(-2 * intersection.vNormal)));
        return new Ray(intersection.point, r, intersection.normal);
    }

    /**
     * Finds the closest intersection point along the given ray.
     * @param ray the ray to trace
     * @return the closest intersection, or null if there are no intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Calculates the local lighting effects at a given intersection point.
     * This includes the object's emission and contributions from all light sources
     * that affect the point (diffuse and specular reflections).
     * @param intersection the intersection point between a ray and a geometry
     * @param k for ensuring the minimum calculation of the color
     * @return the resulting color from local light effects at the intersection
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            // also checks if sign(lNormal) == sign(vNormal)) and if the intersection is unshaded
            if (!setLightSource(intersection, lightSource))
                continue;

            Double3 ktr = transparency(intersection);
            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                color = color
                        .add(iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Calculates the specular reflection component at the intersection point
     * based on the Phong reflection model.
     * @param intersection the intersection data including vectors and material
     * @return the specular reflection as a Double3 coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lNormal));
        double vr = -1 * intersection.v.dotProduct(r);

        return intersection.material.kS.scale(Math.pow(Math.max(0, vr), intersection.material.nSh));
    }

    /**
     * Calculates the diffuse reflection component at the intersection point
     * based on the Phong reflection model.
     * @param intersection the intersection data including normal and material
     * @return the diffuse reflection as a Double3 coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the total color at the intersection point, combining ambient light,
     * emission, and local lighting effects (diffuse + specular).
     * @param intersection the intersection for calculating the color
     * @param ray the viewing ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getVector()))
            return Color.BLACK;

        Color ambientLightIntensity = scene.ambientLight.getIntensity();
        Double3 attenuationCoefficient = intersection.geometry.getMaterial().kA;

        Color intensity = ambientLightIntensity.scale(attenuationCoefficient);
        Color color = calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K);
        return intensity.add(color);
    }

    /**
     * Calculates the total color at an intersection point including local and global lighting effects.
     * @param intersection calculate the color at this intersection
     * @param level the recursion level for global lighting (reflection/refraction)
     * @param k the accumulated transparency/reflection coefficient
     * @return the resulting color at the intersection
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcColorGlobalEffects(intersection, level, k));
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray);
    }
}
