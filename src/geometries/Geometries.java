package geometries;

import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *  The Geometries class represents a collection of geometry shapes
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Geometries extends Intersectable{
    /**
     * List of the geometry shapes
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * An empty constructor
     */
    public Geometries(){
    }

    /**
     * Constructor that creates Geometries with a given list of geometries
     * @param geometries the given list of geometries
     */
    public Geometries(Intersectable... geometries){
        add(geometries);
    }

    /**
     * Adds new geometries to the current geometries
     * @param geometries new given list of geometries for adding
     */
    public void add(Intersectable... geometries){
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    protected List<Intersection>  calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // List that contains all the intersections
        List<Intersection> intersections = null;

        // Loop that goes threw all the geometries and found the intersections
        for (Intersectable geometry : geometries) {
            var geometryIntersections = geometry.calculateIntersections(ray, maxDistance);
            if (geometryIntersections != null)
                if (intersections == null)
                    intersections = new LinkedList<>(geometryIntersections);
                else
                    intersections.addAll(geometryIntersections);
        }
        return intersections;
    }
}
