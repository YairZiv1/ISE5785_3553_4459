package renderer;

import static java.awt.Color.*;

import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;
import sceneTest.Scene;

/**
 * Testing depth of field in rendering
 * @author Yair Ziv and Amitay Yosh'i
 */
public class DepthOfFieldTest {
    /** Default constructor to satisfy JavaDoc generator */
    DepthOfFieldTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for tests */
    private final Scene          scene                  = new Scene("Test scene");

    /** Camera builder for tests */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 80, 500))
            .setDirection(new Point(0, 10, 0), new Vector(0, 1, 2))
            .setVpSize(200, 200).setVpDistance(500)
            .setResolution(800, 800);

    /** Material for tests */
    private final Material material = new Material().setKD(0.3).setKS(0.8).setShininess(100);

    /** Spheres for tests */
    private final Geometry[] objects = {
            new Sphere(new Point(35, 14, 300), 34).setEmission(new Color(BLUE)).setMaterial(
                    new Material().setKD(0.1).setKS(0.8).setShininess(500).setKT(0.7)),
            new Sphere(new Point(-20, 0, 200), 20).setEmission(new Color(ORANGE)).setMaterial(material),
            new Sphere(new Point(15, 6, 150), 26).setEmission(new Color(0, 180, 60)).setMaterial(material),
            new Sphere(new Point(-25, 5, -20), 25).setEmission(new Color(RED)).setMaterial(material),
            new Sphere(new Point(-60, 0, -40), 20).setEmission(new Color(YELLOW)).setMaterial(material),
            new Sphere(new Point(70, -10, -30), 10).setEmission(new Color(CYAN)).setMaterial(material),
            new Sphere(new Point(-120, 0, -90), 20).setEmission(new Color(MAGENTA)).setMaterial(material),
            new Sphere(new Point(50, 0, -100), 20).setEmission(new Color(PINK)).setMaterial(material),
            new Sphere(new Point(120, 0, -250), 20).setEmission(new Color(50, 80, 100)).setMaterial(material),
            new Sphere(new Point(10, 0, -400), 20).setEmission(new Color(75, 0, 130)).setMaterial(material)
    };

    /** Polygon for tests */
    private final Geometry groundPlane = new Polygon(
            new Point(-300, -20, 300),
            new Point(300, -20, 300),
            new Point(300, -20, -500),
            new Point(-300, -20, -500)
    ).setEmission(new Color(30, 30, 30)).setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(20));

    /** Produce a picture of spheres on polygon without depth of field */
    @Test
    void withoutDoF() {
        // Add objects and ground
        scene.geometries.add(groundPlane);
        for (Geometry g : objects)
            scene.geometries.add(g);

        // Light sources
        scene.lights.add(new DirectionalLight(
                new Color(40, 40, 40),
                new Vector(0, -1, 0)));

        scene.lights.add(new DirectionalLight(
                new Color(80, 50, 30),
                new Vector(1, -1, -1)));

        scene.lights.add(new PointLight(
                new Color(800, 500, 0),
                new Point(120, 100, -100)).setKl(0.001).setKq(0.0002));

        scene.lights.add(new SpotLight(
                new Color(400, 400, 400),
                new Point(-40, 70, -25),
                new Vector(0, -1, 0))
                .setKl(0.001).setKq(0.0001));

        camera //
                .build()
                .renderImage()
                .writeToImage("Without Depth of Field spheres");
    }

    /** Produce a picture of spheres on polygon with depth of field */
    @Test
    void withDoF() {
        // Add objects and ground
        scene.geometries.add(groundPlane);
        for (Geometry g : objects)
            scene.geometries.add(g);

        // Light sources
        scene.lights.add(new DirectionalLight(
                new Color(40, 40, 40),
                new Vector(0, -1, 0)));

        scene.lights.add(new DirectionalLight(
                new Color(80, 50, 30),
                new Vector(1, -1, -1)));

        scene.lights.add(new PointLight(
                new Color(800, 500, 0),
                new Point(120, 100, -100)).setKl(0.001).setKq(0.0002));

        scene.lights.add(new SpotLight(
                new Color(400, 400, 400),
                new Point(-40, 70, -25),
                new Vector(0, -1, 0))
                .setKl(0.001).setKq(0.0001));

        camera //
                .setAperture(15, 520, 81)
                .build()
                .renderImage()
                .writeToImage("With Depth of Field spheres");
    }
}
