package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;

/**
 *  The Geometries class represents a collection of geometry shapes
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Geometries extends Intersectable{
    /**
     * The maximum number of objects allowed in a leaf node of the bounding box hierarchy.
     */
    private static final int MAX_OBJECTS_IN_LEAF = 2;

    /**
     * List of the geometry shapes
     */
    private List<Intersectable> geometries = new LinkedList<>();

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
     * Constructor that creates Geometries with a given list of geometries
     * @param geometries the given list of geometries
     */
    public Geometries(List<Intersectable> geometries){
        add(geometries.toArray(new Intersectable[0]));
    }

    /**
     * Adds new geometries to the current geometries
     * @param geometries new given list of geometries for adding
     */
    public void add(Intersectable... geometries){
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Adds new geometries to the current geometries
     * @param geometries new given list of geometries for adding
     */
    public void add(List<Intersectable> geometries){
        Collections.addAll(this.geometries, geometries.toArray(new Intersectable[0]));
    }

    /**
     * Getter for geometries
     * @return the list of geometries
     */
    public List<Intersectable> getGeometries() {
        return geometries;
    }

    @Override
    public Geometries move(Vector offset) {
        Geometries translated = new Geometries();
        for (Intersectable geo : geometries) {
            translated.add(geo.move(offset));
        }
        return translated;
    }

    @Override
    protected List<Intersection>  calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Test if the ray intersects the bounding box of the geometries
        if (boundingBox != null && boundingBox.isNotIntersected(ray)) return null;

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

    @Override
    protected BoundingBox calculateBoundingBox() {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geometry : geometries) {
            BoundingBox boundingBox = geometry.calculateBoundingBox();

            Point min = boundingBox.getMin();
            Point max = boundingBox.getMax();

            minX = Math.min(minX, min.getX());
            minY = Math.min(minY, min.getY());
            minZ = Math.min(minZ, min.getZ());
            maxX = Math.max(maxX, max.getX());
            maxY = Math.max(maxY, max.getY());
            maxZ = Math.max(maxZ, max.getZ());
        }

        return new BoundingBox(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Creates a Conservative Bounding Region (CBR) for the geometries.
     */
    public void createCBR() {
        for (Intersectable geometry : geometries) {
            geometry.setBoundingBox();
        }
    }

    /**
     * Creates a Bounding Volume Hierarchy (BVH) for the geometries.
     * This method first creates bounding boxes for each geometry and then recursively builds the BVH.
     */
    public void createBVH() {
        this.createCBR();
        if (!geometries.isEmpty())
            geometries = createBVHRecursiveHelper(geometries, 0).geometries;
    }

    /**
     * A helper method to recursively create a Bounding Volume Hierarchy (BVH).
     * It sorts the geometries based on their center along the axis determined by the current depth,
     * and splits them into two halves until the maximum number of objects in a leaf is reached.
     * @param objects the list of Intersectable geometries to be organized into a BVH
     * @param depth the current depth in the BVH tree
     * @return a Geometries object representing the BVH node containing the geometries
     */
    private Geometries createBVHRecursiveHelper(List<Intersectable> objects, int depth) {
        if (objects.size() <= MAX_OBJECTS_IN_LEAF) {
            Geometries leaf = new Geometries(objects);
            leaf.setBoundingBox(); // Set the bounding box for the new leaf node
            return leaf; // Leaf node with geometries
        }

        // Choose the axis based on the current depth
        int axis = depth % 3; // 0-X, 1-Y, 2-Z
        objects.sort(Comparator.comparingDouble(geometry -> geometry.boundingBox.getCenter(axis)));

        // Split the list into two halves
        int mid = objects.size() / 2;
        List<Intersectable> leftList = objects.subList(0, mid);
        List<Intersectable> rightList = objects.subList(mid, objects.size());

        // Recursively create BVH for the left and right halves
        Geometries left = createBVHRecursiveHelper(new ArrayList<>(leftList), depth + 1);
        Geometries right = createBVHRecursiveHelper(new ArrayList<>(rightList), depth + 1);

        // Create a new Geometries object that contains the left and right BVH nodes
        Geometries node = new Geometries(left, right);
        node.setBoundingBox(); // Set the bounding box for the new node
        return node;
    }
}
