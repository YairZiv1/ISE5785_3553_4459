package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *  The Geometries class represents collection of geometries shapes
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Geometries extends Intersectable{
    /**
     * List of the geometries shapes
     */
    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

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
    protected List<Intersection>  calculateIntersectionsHelper(Ray ray) {
        // List that contains all the intersections
        List<Intersection> intersections = null;

        // Loop that go threw all the geometries and find the intersections
        for (Intersectable geometry : geometries) {
            var geometryIntersections = geometry.calculateIntersections(ray);
            if (geometryIntersections != null)
                if (intersections == null)
                    intersections = new LinkedList<>(geometryIntersections);
                else
                    intersections.addAll(geometryIntersections);
        }
        return intersections;
    }
}
