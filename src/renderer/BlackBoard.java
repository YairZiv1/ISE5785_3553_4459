package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

/**
 * Represents a "target area" in space used for generating beams of rays
 * @author Yair Ziv and Amitay Yosh'i
 */
public class BlackBoard {
    /**
     * The vector represents the horizontal axis of the target area
     */
    private final Vector vX;
    /**
     * The vector represents the vertical axis of the target area
     */
    private final Vector vY;
    /**
     * The number of rays to generate along one side of the target area
     * Or the square root of the total number of rays to generate
     */
    private final int raysPerSide;
    /**
     * The physical side length of the target area
     */
    private final double sideSize;
    /**
     * Point that represents the center of the target area
     */
    private final Point targetPoint;
    /**
     * Random number generator used for creating jittered and circular sampling patterns
     */
    private final Random rand = new Random();

    /**
     * Constructs a BlackBoard object that represents a target area in space.
     * @param centralRay the main ray through the middle of the target area
     * @param targetPoint the point the ray intersects on the target area
     * @param sideSize the physical side length of the target area
     * @param raysPerSide the number of rays to generate along one side of the target area
     */
    public BlackBoard(Ray centralRay, Point targetPoint, double sideSize, int raysPerSide) {
        // Although it is apparently more accurate to send the axes so that the target surface
        // is on the plane of the view plane, which is not always perpendicular to the beam,
        // we implemented it in the following way, because in the exercise they explicitly said that
        // the target surface is perpendicular to the beam (usually from the camera).
        Vector direction = centralRay.getVector();

        // Choose an "vUp" vector not parallel to direction vector
        Vector vUp = !isZero(Math.abs(direction.dotProduct(Vector.AXIS_Y)) - 1)
                ? Vector.AXIS_Y
                : Vector.AXIS_X;

        this.vX = direction.crossProduct(vUp).normalize();
        this.vY = this.vX.crossProduct(direction).normalize();
        this.raysPerSide = raysPerSide;
        this.sideSize = sideSize;
        this.targetPoint = targetPoint;
    }

    /**
     * Generates a list of points representing the sampling locations on the target area.
     * The points are generated in a jittered pattern inside a square grid.
     * @return a list of points representing the sampling locations
     */
    public List<Point> generateJitteredGrid() {
        // List of points representing the sampling locations
        List<Point> samplePoints = new ArrayList<>(raysPerSide * raysPerSide);

        // Each sample ray will be in a square cell of size sideSize / raysPerSide
        double cellSize = sideSize / raysPerSide;

        for (int i = 0; i < raysPerSide; ++i) {
            for (int j = 0; j < raysPerSide; ++j) {

                // Each sample point places the center of the cell and adds the jitter
                double offsetX = (i + 0.5 - (double) raysPerSide / 2) * cellSize;
                double offsetY = (j + 0.5 - (double) raysPerSide / 2) * cellSize;

                // Add jitter inside the cell
                offsetX += (rand.nextDouble() - 0.5) * cellSize;
                offsetY += (rand.nextDouble() - 0.5) * cellSize;

                Point samplePoint = targetPoint;
                // If offsetX is zero than no need to move on the horizontal axis
                if (!isZero(offsetX))
                    samplePoint = samplePoint.add(vX.scale(offsetX));
                // If offsetY is zero than no need to move on the vertical axis
                if (!isZero(offsetY))
                    samplePoint = samplePoint.add(vY.scale(offsetY));

                samplePoints.add(samplePoint);
            }
        }
        return samplePoints;
    }

    /**
     * Generates a list of points representing the sampling locations on the target area.
     * The points are generated randomly in a circular pattern within the target area.
     * @return a list of points representing the sampling locations
     */
    public List<Point> generateRandomInCircle() {
        // List of points representing the sampling locations
        List<Point> samplePoints = new ArrayList<>(raysPerSide * raysPerSide);

        for (int i = 0; i < raysPerSide * raysPerSide; ++i) {

            double offsetDistance = rand.nextDouble() * sideSize / 2; // Random offset distance within the pixel
            double offsetAngle = rand.nextDouble() * 2 * Math.PI; // Random angle for the offset

            double offsetX = Math.sin(offsetAngle) * offsetDistance;
            double offsetY = Math.cos(offsetAngle) * offsetDistance;

            Point samplePoint = targetPoint;
            // If offsetX is zero than no need to move on the horizontal axis
            if (!isZero(offsetX))
                samplePoint = samplePoint.add(vX.scale(offsetX));
            // If offsetY is zero than no need to move on the vertical axis
            if (!isZero(offsetY))
                samplePoint = samplePoint.add(vY.scale(offsetY));

            samplePoints.add(samplePoint);
        }
        return samplePoints;
    }
}
