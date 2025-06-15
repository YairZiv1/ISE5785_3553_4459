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
     * Constructs a BlackBoard using jittered sampling inside a square grid.
     * @param centralRay the main ray through the middle of the target area
     * @param targetPoint the point the ray intersects on the target area
     * @param sideSize the physical side length of the target area
     * @param numOfRays the number of rays in total (must be a perfect square)
     */
    public BlackBoard(Ray centralRay, Point targetPoint, double sideSize, int numOfRays) {
        // Although it is apparently more accurate to send the axes so that the target surface
        // is on the plane of the view plane, which is not always perpendicular to the beam,
        // we implemented it in the following way, because in the exercise they explicitly said that
        // the target surface is perpendicular to the beam (usually from the camera).
        Vector direction = centralRay.getVector().normalize();

        // Choose an "vUp" vector not parallel to direction vector
        Vector vUp = !isZero(Math.abs(direction.dotProduct(Vector.AXIS_Y)) - 1)
                ? Vector.AXIS_Y
                : Vector.AXIS_X;

        Vector vRight = direction.crossProduct(vUp).normalize();
        vUp = vRight.crossProduct(direction).normalize();

        // Round the sideSize to the nearest square root below or equal to the sideSize
        int sqrtRays = (int) Math.sqrt(numOfRays);
        // Each sample ray will be in a square cell of size sideSize / sqrtRays
        double cellSize = sideSize / sqrtRays;

        Random rand = new Random();
        samplePoints = new ArrayList<>(numOfRays);

        for (int i = 0; i < sqrtRays; ++i) {
            for (int j = 0; j < sqrtRays; ++j) {
                // Add jitter inside the cell
                double jitterX = (rand.nextDouble() - 0.5) * cellSize;
                double jitterY = (rand.nextDouble() - 0.5) * cellSize;

                // Each sample point places the center of the cell and adds the jitter
                double offsetX = (i + 0.5) * cellSize + jitterX;
                double offsetY = (j + 0.5) * cellSize + jitterY;

                Point samplePoint = targetPoint;

                // If offsetX is zero than no need to move on the horizontal axis
                if (!isZero(offsetX))
                    samplePoint = samplePoint.add(vRight.scale(offsetX));
                // If offsetY is zero than no need to move on the vertical axis
                if (!isZero(offsetY))
                    samplePoint = samplePoint.add(vUp.scale(offsetY));

                samplePoints.add(samplePoint);
            }
        }
    }

    /**
     * Returns the list of sample points generated for this BlackBoard.
     * @return the list of sample points
     */
    public List<Point> getSamplePoints() {
        return samplePoints;
    }
}
