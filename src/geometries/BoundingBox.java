package geometries;

import primitives.Point;
import primitives.Ray;

/**
 * The BoundingBox class represents an Axis-Aligned Bounding Box (AABB) in 3D space.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class BoundingBox {
    /**
     * The minimum 3D point corner of the bounding box.
     */
    private final Point min;
    /**
     * The maximum 3D point corner of the bounding box.
     */
    private final Point max;

    /**
     * Constructs a Box object with the specified minimum and maximum points.
     * @param min the minimum point of the box
     * @param max the maximum point of the box
     */
    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Getter for the minimum point of the box.
     * @return the minimum point of the box
     */
    public Point getMin() {
        return min;
    }

    /**
     * Getter for the maximum point of the box.
     * @return the maximum point of the box
     */
    public Point getMax() {
        return max;
    }

    /**
     * Calculates the center of this bounding box along a specified axis.
     * @param axis the axis along which to calculate the center (0 for X, 1 for Y, 2 for Z)
     * @return the center coordinate along the specified axis
     */
    public double getCenter(int axis) {
        return switch (axis) {
            case 0 -> (min.getX() + max.getX()) / 2;
            case 1 -> (min.getY() + max.getY()) / 2;
            case 2 -> (min.getZ() + max.getZ()) / 2;
            default -> 0;
        };
    }

    /**
     * Checks whether the given ray intersects this AABB box.
     * Uses the slab method.
     * @param ray the ray to check for intersection
     * @return true if the ray intersects the box, false otherwise
     */
    public boolean isNotIntersected(Ray ray) {
        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        double[] origin = { ray.getPoint(0).getX(), ray.getPoint(0).getY(), ray.getPoint(0).getZ() };
        double[] direction = { ray.getVector().getX(), ray.getVector().getY(), ray.getVector().getZ() };
        double[] minArr = { min.getX(), min.getY(), min.getZ() };
        double[] maxArr = { max.getX(), max.getY(), max.getZ() };

        for (int i = 0; i < 3; ++i) {
            double t1 = (minArr[i] - origin[i]) / direction[i];
            double t2 = (maxArr[i] - origin[i]) / direction[i];

            if (t1 > t2) {
                double tmp = t1;
                t1 = t2;
                t2 = tmp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMax < tMin) return true;
        }
        return false;
    }
}
