package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
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
     * The image writer used to write the final rendered image to a file.
     */
    private ImageWriter imageWriter;
    /**
     * The ray tracer calculates the color of each pixel by tracing rays through the scene.
     */
    private RayTracerBase rayTracer;
    /**
     * The number of pixels across
     */
    private int nX = 1;
    /**
     * The number of pixels along the length
     */
    private int nY = 1;

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
        Point pC = p0.add(vTo.scale(distance));

        // Calculate the size of each pixel (height and width)
        double rY = height / nY;
        double rX = width / nX;

        // Calculate the vertical and horizontal offset from the center to pixel (i, j)
        // Minus because y starts at the top of the matrix and continues opposite the vUp vector
        double yI = -(i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;

        // Start at the center of the view plane
        Point pIJ = pC;
        // If xJ is zero than no need to move on the horizontal axis
        if (!isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        // If yI is zero than no need to move on the vertical axis
        if (!isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));

        // Return the ray that starts at camera and goes through the center of the pixel
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * The method will go through all the pixels according to the resolution,
     * and perform ray tracing to color all the pixels of the image.
     * @return A camera
     */
    public Camera renderImage() {
        for(int i=0; i < nX; i++)
            for(int j=0; j < nY; j++)
                castRay(i,j);
        return this;
    }

    /**
     * This method prints a grid on an image.
     * @param interval the number of pixels in the width/height of the grid square
     * @param color the color of the grid
     * @return A camera
     */
    public Camera printGrid(int interval, Color color) {
        for(int i=0; i < nX; i+=interval)
            for(int j=0; j < nY; j++)
                imageWriter.writePixel(j, i, color);
        for(int i=0; i < nX; i++)
            for(int j=0; j < nY; j+=interval)
                imageWriter.writePixel(j, i, color);
        return this;
    }

    /**
     * Function writeToImage produces unoptimized png file of the image according
     * to pixel color matrix in the directory of the project, using delegation.
     * @param imageName the name of png file
     * @return A camera
     */
    public Camera writeToImage(String imageName) {
        imageWriter.writeToImage(imageName);
        return this;
    }

    /**
     * This method colors a pixel.
     * @param i the pixel's row number
     * @param j the pixel's column number
     */
    private void castRay(int i,  int j) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        this.imageWriter.writePixel(j, i, color);
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
            camera.vTo = targetPoint.subtract(camera.p0);
            camera.vRight = camera.vTo.crossProduct(vUp);
            camera.vUp = camera.vRight.crossProduct(camera.vTo);

            camera.vTo = camera.vTo.normalize();
            camera.vRight = camera.vRight.normalize();
            camera.vUp = camera.vUp.normalize();
            return this;
        }

        /**
         * In case the target point is exactly "above" the camera (the camera direction will be with the Y axis),
         * an exception should be thrown because the cross product result will be the zero vector.
         * @param targetPoint the camera's target point (what point the photographer is aiming at)
         * @return A camera
         */
        public Builder setDirection(Point targetPoint) {
            return setDirection(targetPoint, Vector.AXIS_Y);
        }

        /**
         * Set the size of the view plane.
         * @param width the width of the view plane
         * @param height the height of the view plane
         * @return A camera
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0)
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
            if (alignZero(distance) <= 0)
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
            camera.nX = nX;
            camera.nY = nY;
            return this;
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

            if (camera.nX <= 0)
                throw new MissingResourceException(description, className, "nX");
            if (camera.nY <= 0)
                throw new MissingResourceException(description, className, "nY");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            if (camera.vRight == null) {
                camera.vRight = camera.vTo.crossProduct(camera.vUp);
                camera.vRight = camera.vRight.normalize();
            }

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

            if (alignZero(camera.distance) <= 0)
                throw new IllegalArgumentException("distance must be positive");
            if (alignZero(camera.width) <= 0 || alignZero(camera.height) <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * For now, set rayTracer if the type is simple, otherwise set rayTracer to null
         * @param scene the scene that will be rendered using this ray tracer
         * @param rayTracerType the type of the rayTracer
         * @return A camera
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            if (rayTracerType == RayTracerType.SIMPLE)
                camera.rayTracer = new SimpleRayTracer(scene);
            else
                camera.rayTracer = null;
            return this;
        }
    }
}
