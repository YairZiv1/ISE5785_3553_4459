package renderer;

import geometries.BoundingBox;
import primitives.*;
import primitives.Vector;
import scene.Scene;

import java.util.*;
import java.util.stream.*;

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
     * The number of rays to be cast for each side of a pixel in the image for antialiasing effect
     */
    private int raysPerSideAA = 1;
    /**
     * The size of the aperture for depth of field (DoF) effect
     */
    private double apertureSize = 0;
    /**
     * The focal distance for depth of field (DoF) effect
     */
    private double focalDistance = 1;
    /**
     * The number of rays to be cast for each side of the aperture for depth of field (DoF) effect
     */
    private int raysPerSideDoF = 1;

    /**
     * Enable CBR optimization
     */
    private boolean enableCBR = false;
    /**
     * Enable BVH optimization
     */
    private boolean enableBVH = false;

    /** Number of threads to use fore rendering image by the camera */
    private int              threadsCount     = 0;
    /**
     * Number of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS    = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero, there is no progress output
     */
    private double           printInterval    = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Camera empty constructor
     */
    private Camera() {}

    /**
     * Builder getter
     * @return the camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Calculates the ray from camera through a specific pixel with a given resolution.
     * @param nX the number pixels columns
     * @param nY the number pixels rows
     * @param j the pixel's column number
     * @param i the pixel's row number
     * @return a ray that starts at the camera and goes through the pixel (i, j)
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
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case-1 -> renderImageStream();
            default-> renderImageRawThreads();
        };
    }

    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nX; i++)
            for (int j = 0; j < nY; j++)
                castRay(i, j);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignore) {}
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel() //
                .forEach(i-> IntStream.range(0, nX).parallel() //
                        .forEach(j-> castRay(j, i)));
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
     * Function writeToImage produces an unoptimized png file of the image according
     * to pixel color matrix in the directory of the project, using delegation.
     * @param imageName the name of png file
     * @return A camera
     */
    public Camera writeToImage(String imageName) {
        imageWriter.writeToImage(imageName);
        return this;
    }

    /**
     * This method colors a pixel by casting a ray or many rays through it.
     * @param i the pixel's row number
     * @param j the pixel's column number
     */
    private void castRay(int i, int j) {
        Color color;
        Ray centralRay = constructRay(nX, nY, j, i);

        if (raysPerSideAA > 1) // If multiple rays are used, cast a beam of rays
            color = castRayAntiAliasing(centralRay);
        else if (apertureSize > 0 && raysPerSideDoF > 1) // If the depth of field is used...
            color = castRayDepthOfField(centralRay);
        else // If no antialiasing and no depth of field, use a single ray
            color = rayTracer.traceRay(centralRay);

        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * This method creates a beam of rays through a pixel and returns the averaged color.
     * @param centralRay the ray that goes through the pixel
     * @return the average color from all rays in the beam
     */
    private Color castRayAntiAliasing(Ray centralRay) {
        Point targetPoint = centralRay.getPoint(distance);

        List<Point> samplePoints = new BlackBoard(centralRay, targetPoint, width / nX, raysPerSideAA)
                .generateSamplePoints();

        Color totalColor = Color.BLACK;
        for (Point samplePoint : samplePoints) {
            Ray sampleRay = new Ray(this.p0, samplePoint.subtract(this.p0));
            if (apertureSize > 0 && raysPerSideDoF > 1) // If the depth of field is used...
                totalColor = totalColor.add(castRayDepthOfField(sampleRay));
            else // If no depth of field, use a single ray
                totalColor = totalColor.add(rayTracer.traceRay(sampleRay));
        }

        return totalColor.reduce(samplePoints.size());
    }

    /**
     * This method colors a pixel using depth of field (DoF) effect.
     * @param centralRay the ray that goes through the pixel
     * @return the average color from all rays in the aperture
     */
    private Color castRayDepthOfField(Ray centralRay) {
        Point focalPoint = centralRay.getPoint(focalDistance);

        List<Point> aperturePoints = new BlackBoard(centralRay, this.p0, apertureSize, raysPerSideDoF)
                .generateSamplePoints();

        Color totalColor = Color.BLACK;
        for (Point aperturePoint : aperturePoints) {
            Ray originRay = new Ray(aperturePoint, focalPoint.subtract(aperturePoint));
            totalColor = totalColor.add(rayTracer.traceRay(originRay));
        }

        return totalColor.reduce(aperturePoints.size());
    }

    /**
     * Class for building the camera
     */
    public static class Builder {
        /** Default constructor to satisfy JavaDoc generator */
        Builder() { /* to satisfy JavaDoc generator */ }

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
         * an exception should be thrown because the cross-product result will be the zero vectors.
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

        /**
         * Set the number of rays to be cast for each side of a pixel in the image.
         * @param raysPerSide the number of rays to be cast for each side of a pixel
         * @return A camera
         */
        public Builder setAntiAliasingResolution(int raysPerSide) {
            // We are not doing the adaptive super sampling improvement, so raysPerSide not must be a power of 2 plus 1
            if (raysPerSide < 1)
                throw new IllegalArgumentException("number of rays per side must be positive");

            camera.raysPerSideAA = raysPerSide;
            return this;
        }

        /**
         * Set the aperture size, focal distance, and number of rays for depth of field (DoF) effect.
         * @param apertureSize the size of the aperture
         * @param focalDistance the distance at which the camera is focused
         * @param raysPerSide the number of rays to be cast for each side of the aperture
         * @return A camera
         */
        public Builder setAperture(double apertureSize, double focalDistance, int raysPerSide) {
            if (alignZero(apertureSize) < 0)
                throw new IllegalArgumentException("aperture size must be non-negative");
            if (alignZero(focalDistance) <= 0)
                throw new IllegalArgumentException("focal distance must be positive");
            // We are not doing the adaptive super sampling improvement, so raysPerSide not must be a power of 2 plus 1
            if (raysPerSide < 1)
                throw new IllegalArgumentException("number of rays per side must be positive");

            camera.apertureSize = apertureSize;
            camera.focalDistance = focalDistance;
            camera.raysPerSideDoF = raysPerSide;
            return this;
        }

        /**
         * Enable CBR optimization.
         * CBR is not compatible with BVH, so if BVH is enabled, it will be disabled.
         * @return A camera
         */
        public Builder enableCBR() {
            camera.enableCBR = true;
            camera.enableBVH = false; // CBR is not compatible with BVH
            return this;
        }

        /**
         * Enable BVH optimization.
         * BVH is not compatible with CBR, so if CBR is enabled, it will be disabled.
         * @return A camera
         */
        public Builder enableBVH() {
            camera.enableBVH = true;
            camera.enableCBR = false; // BVH is not compatible with CBR
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param  threads number of threads
         * @return         builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param  interval printing interval in %
         * @return          builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
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

            if (!isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !isZero(camera.vUp.dotProduct(camera.vRight)))
                throw new IllegalArgumentException("vTo, vUp, vRight must be orthogonal");

            if (alignZero(camera.distance) <= 0)
                throw new IllegalArgumentException("distance must be positive");
            if (alignZero(camera.width) <= 0 || alignZero(camera.height) <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.enableCBR)
                camera.rayTracer.scene.geometries.createCBR();
            else if (camera.enableBVH)
                camera.rayTracer.scene.geometries .createBVH();

            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
