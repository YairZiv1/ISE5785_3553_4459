package renderer;

import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class represents a camera in 3D space.
 * @author Yair Ziv and Amitay Yosh'i.
 */
public class Camera implements Cloneable {
    /**
     * Point that represents the location of the camera
     */
    private Point p0;
    /**
     * Vector that represents the direction the camera is pointing
     */
    private Vector vTo;
    /**
     * Vector that represents the direction up from the camera
     */
    private Vector vUp;
    /**
     * Vector that represents the direction right from the camera
     */
    private Vector vRight;

    /**
     * The distance between camera and view plane
     */
    private double distance = 0;
    /**
     * The width of the view plane
     */
    private double width = 0;
    /**
     * The height of the view plane
     */
    private double height = 0;

    /**
     * Camera empty constructor
     */
    private Camera() {}

    /**
     * Builder getter
     *
     * @return the camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Calculates the ray from camera through a specific pixel with a given resolution
     * @param nX the number of columns of pixels.
     * @param nY the number of rows of pixels.
     * @param j the pixel's column number
     * @param i the pixel's row number.
     * @return the ray from camera to the middle of the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the center point of the view plane
        Point pC = this.p0.add(vTo.scale(distance));

        // Calculate the size of each pixel (height and width)
        double rY = this.height / nY;
        double rX = this.width / nX;

        // Calculate the vertical and horizontal offset from the center to pixel (i, j)
        // Minus because y starts at the top of the matrix and continues opposite the vUp vector
        double yI = -(i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;

        // Start at the center of the view plane
        Point pIJ = pC;
        // If xJ is zero than no need to move on the horizontal axis
        if (!isZero(xJ))
            pIJ = pIJ.add(this.vRight.scale(xJ));
        // If yI is zero than no need to move on the vertical axis
        if (!isZero(yI))
            pIJ = pIJ.add(this.vUp.scale(yI));

        // Return the ray that starts at camera and goes through the center of the pixel
        return new Ray(this.p0, pIJ.subtract(this.p0));
    }

    /**
     * Class for building the camera
     */
    public static class Builder {
        /**
         * Camera object
         */
        private final Camera camera = new Camera();

        /**
         * Set the location of the camera
         * @param p0 the location of the camera
         * @return A camera
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Ensures that vector up orthogonal to vector to, then initialize them.
         * @param vTo the direction the camera is pointing
         * @param vUp the direction up from the camera
         * @return A camera
         * @throws IllegalArgumentException if vector up isn't orthogonal to vector to
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo isn't orthogonal to vUp");

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Calculating the vector to, vector right, and the "exact" vector up
         * @param targetPoint The camera's target point (what point the photographer is aiming at)
         * @param vUp the direction up from the camera (Not necessarily the "exact" vector)
         * @return A camera
         */
        public Builder setDirection(Point targetPoint, Vector vUp) {
            camera.vTo = targetPoint.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp);
            camera.vUp = camera.vRight.crossProduct(camera.vTo);
            return this;
        }

        /**
         * In the case the target point is exactly "above" the camera (the camera direction will be with the Y axis),
         * an exception should be thrown because the cross product result will be the zero vector.
         * @param targetPoint the camera's target point (what point the photographer is aiming at)
         * @return A camera
         */
        public Builder setDirection(Point targetPoint) {
            camera.vUp = Vector.AXIS_Y;
            camera.vTo = targetPoint.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();

            camera.vTo = camera.vTo.normalize();
            camera.vRight = camera.vRight.normalize();
            camera.vUp = camera.vUp.normalize();

            return this;
        }

        /**
         * Set the size of the view plane.
         * @param width the width of the view plane
         * @param height the height of the view plane
         * @return A camera
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Set the distance between camera and view plane.
         * @param distance the distance between camera and view plane
         * @return A camera
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("distance must be positive");

            camera.distance = distance;
            return this;
        }

        /**
         * Set the resolution of the view plane.
         * @param nX number of pixels across (like width)
         * @param nY number of pixels along the length (like height)
         * @return A camera
         */
        public Builder setResolution(int nX, int nY) {
            return null;
        }

        /**
         * Checking the camera data and intelligizing vector Right
         * @return a clone of intelligized camera
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "Missing Render Data:";

            if (camera.p0 == null)
                throw new MissingResourceException(description, className, "p0");
            if (camera.vTo == null)
                throw new MissingResourceException(description, className, "vTo");
            if (camera.vUp == null)
                throw new MissingResourceException(description, className, "vUp");

            if (camera.distance == 0)
                throw new MissingResourceException(description, className, "distance");
            if (camera.width == 0)
                throw new MissingResourceException(description, className, "width");
            if (camera.height == 0)
                throw new MissingResourceException(description, className, "height");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (!isZero(camera.vTo.length() - 1) ||
                    !isZero(camera.vUp.length() - 1) ||
                    !isZero(camera.vRight.length() - 1))
                throw new IllegalArgumentException("vTo, vUp, vRight must be normalized");

            double x = camera.vTo.dotProduct(camera.vUp);
            double y = camera.vTo.dotProduct(camera.vRight);
            double z = camera.vUp.dotProduct(camera.vUp);

            if (!isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !isZero(camera.vUp.dotProduct(camera.vRight)))
                throw new IllegalArgumentException("vTo, vUp, vRight must be orthogonal");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance must be positive");
            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
