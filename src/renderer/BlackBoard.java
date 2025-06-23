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
     * List of points representing the sampling locations
     */
    private final List<Point> samplePoints;

    /**
     * The point where the central ray intersects the target area
     */
    private final Point targetPoint;
    /**
     * The horizontal axis of the target area
     */
    private final Vector vX;
    /**
     * The vertical axis of the target area
     */
    private final Vector vY;
    /**
     * The size of each cell's side in the grid
     */
    private final double cellSize;
    /**
     * The number of rays to generate along one side of the target area
     */
    private final int raysPerSide;

    /**
     * Constructs a BlackBoard using a square grid and adding jitter.
     * @param centralRay the main ray through the middle of the target area
     * @param targetPoint the point the ray intersects on the target area
     * @param sideSize the physical side length of the target area
     * @param raysPerSide the number of rays to generate along one side of the target area
     */
    public BlackBoard(Ray centralRay, Point targetPoint, double sideSize, int raysPerSide) {
        this.targetPoint = targetPoint;
        this.raysPerSide = raysPerSide;

        // Although it is apparently more accurate to send the axes so that the target surface
        // is on the plane of the view plane, which is not always perpendicular to the beam,
        // we implemented it in the following way, because in the exercise they explicitly said that
        // the target surface is perpendicular to the beam (usually from the camera).
        Vector direction = centralRay.getVector();

        // Choose an "up" vector not parallel to direction vector
        Vector up = !isZero(Math.abs(direction.dotProduct(Vector.AXIS_Y)) - 1)
                ? Vector.AXIS_Y
                : Vector.AXIS_X;
        vX = direction.crossProduct(up);
        vY = vX.crossProduct(direction);

        // Each sample ray will be in a square cell of size sideSize / raysPerSide
        cellSize = sideSize / raysPerSide;

        samplePoints = new ArrayList<>();
        for (int i = 0; i < raysPerSide * raysPerSide; ++i) {
            samplePoints.add(null);
        }
    }

    /**
     * Generates sample points on the target area using a grid pattern with jitter.
     * @return the list of sample points
     */
    public List<Point> generateSamplePoints() {
        Random rand = new Random();

        for (int i = 0; i < raysPerSide; ++i) {
            for (int j = 0; j < raysPerSide; ++j) {
                // Each sample point places the center of the cell and adds the jitter
                double offsetX = (i + 0.5 - (float) raysPerSide / 2) * cellSize;
                double offsetY = (j + 0.5 - (float) raysPerSide / 2) * cellSize;

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

                samplePoints.set(i * raysPerSide + j, samplePoint);
            }
        }
        return samplePoints;
    }
}
