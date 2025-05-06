package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing integration between creating rays from a camera and calculating ray intersections with geometries.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class CameraIntersectionsIntegrationTest {
    /** Camera for the tests */
    private final Camera camera = Camera.getBuilder()
            .setLocation(new Point(0,0,0.5))
            .setVpDistance(1)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(3, 3)
            .build();

    /**
     * A method to help calculate the sum of the intersection points of all rays
     * emanating from the camera with a given geometry.
     *
     * @param intersectionsExpected the number of expected intersection points.
     * @param geometry              the geometry to calculate the intersection points with.
     * @param message               the message to return in case there was a problem.
     */
    private void assertNumOfIntersections(int intersectionsExpected,
                                          Intersectable geometry,
                                          int nX,
                                          int nY,
                                          String message
    ) {
        int intersectionsCounter = 0;

        // Loop through each pixel in the view plane
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                // Construct a ray from the camera through the pixel
                Ray ray = camera.constructRay(nX,nY,j,i);
                // Find intersection points of the ray with the given geometry
                var intersectionsList = geometry.findIntersections(ray);
                // If there are intersections, add the count to the total
                if (intersectionsList != null)
                    intersectionsCounter += intersectionsList.size();
            }
        }
        // Assert that the actual number of intersections matches the expected number
        assertEquals(intersectionsExpected, intersectionsCounter, message);
    }

    /**
     * Test method for integration with Sphere.
     */
    @Test
    void testSphereIntegration() {
        // TC01: Small sphere in front of the camera
        Sphere sphere = new Sphere(1, new Point(0,0,-3));
        assertNumOfIntersections(2, sphere, 3, 3,
                "Intersections through center pixel only");

        // TC02: Large sphere close to the view plane
        sphere = new Sphere(2.5, new Point(0,0,-2.5));
        assertNumOfIntersections(18, sphere, 3, 3,
                "Intersections through all pixels");

        // TC03: Medium-sized sphere, intersects all pixels except the corners
        sphere = new Sphere(2, new Point(0,0,-2));
        assertNumOfIntersections(10, sphere, 3, 3,
                "Intersections through not in corners pixels");

        // TC04: Camera inside sphere, every ray intersects the sphere once
        sphere = new Sphere(4, Point.ZERO);
        assertNumOfIntersections(9, sphere, 3, 3,
                "Camera inside sphere");

        // TC05: Sphere behind the camera
        sphere = new Sphere(0.5, new Point(0,0,1));
        assertNumOfIntersections(0, sphere, 3, 3,
                "Sphere behind camera");
    }

    /**
     * Test method for integration with Plane.
     */
    @Test
    void testPlaneIntegration() {
        // TC01: Plane's normal parallel to camera's direction
        Plane plane = new Plane(new Point(0,0,-3), new Vector(0,0,-1));
        assertNumOfIntersections(9, plane, 3, 3,
                "Plane's normal parallel to camera's direction");

        // TC02: Tilted plane, but still intersects all rays
        plane = new Plane(new Point(0,0,-3), new Vector(0,1,-3));
        assertNumOfIntersections(9, plane, 3, 3,
                "Plane with high slope");

        // TC03:  Plane with a shallow angle, some rays miss it
        plane = new Plane(new Point(0,0,-3), new Vector(0,3,-1));
        assertNumOfIntersections(6, plane, 3, 3,
                "Plane with low slope");
    }

    /**
     * Test method for integration with Triangle.
     */
    @Test
    void testTriangleIntegration() {
        // TC01: Small triangle in front of the camera
        Triangle triangle = new Triangle(
                new Point(0,1,-2), new Point(1,-1,-2), new Point(-1,-1,-2));
        assertNumOfIntersections(1, triangle, 3, 3,
                "Intersections through center pixel only");

        // TC02: Triangle stretched upward
        triangle = new Triangle(
                new Point(0,20,-2), new Point(1,-1,-2), new Point(-1,-1,-2));
        assertNumOfIntersections(2, triangle, 3, 3,
                "Intersections through center and upper pixels only");
    }
}
