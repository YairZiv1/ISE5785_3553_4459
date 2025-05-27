package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

public class FullImageTest {
    /** Default constructor to satisfy JavaDoc generator */
    FullImageTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for the tests */
    private final Scene scene = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Generating image with all the effects.
     */
    @Test
    void allEffects() {
            scene.geometries.add(new Sphere(new Point(0,3,0), 20).setEmission(new Color(BLUE))
                    .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(200).setKT(0.3)));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(150, 150) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("zzz");
    }
}
