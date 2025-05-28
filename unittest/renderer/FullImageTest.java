package renderer;

import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import java.util.Random;

import geometries.*;
import primitives.*;
import sceneTest.Scene;

import static java.awt.Color.YELLOW;

public class FullImageTest {
    /** Default constructor to satisfy JavaDoc generator */
    FullImageTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for the tests */
    Scene sceneTest = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(sceneTest, RayTracerType.SIMPLE);

    @Test
    void allEffects() {
        Random rand = new Random();
        int numStars = 250; // 250
        double starMinZ = -200;
        double starMaxZ = 120;

        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 500 - 250;
            double y = rand.nextDouble() * 400 + 100;
            double z = rand.nextDouble() * (starMaxZ - starMinZ) + starMinZ;

            double radius = rand.nextDouble() * 1.3 + 0.3;

            Color starColor = new Color(
                    200 + rand.nextInt(56),
                    200 + rand.nextInt(56),
                    200 + rand.nextInt(56));

            sceneTest.geometries.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starColor)
                            .setMaterial(new Material().setKD(0.5).setShininess(200))
            );
        }

        // alien
        Color alienEmission = new Color(60, 192, 60);
        Material alienMaterial = new Material().setKD(0.3).setKS(0.6).setShininess(150).setKT(0.2);

        Geometry alienHead = new Sphere(new Point(0,100,63), 4)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienBody = new Cylinder(1, new Ray(new Point(0,100,59), Vector.AXIS_Z.scale(-1)), 5)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienRightLeg = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(1,0,-2)), 8)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienLeftLeg = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(-1,0,-2)), 8)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienRightHand = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(1,0,1)), 8)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienLeftHand = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(-1,0,1)), 8)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienRightFeeler = new Cylinder(1, new Ray(new Point(1,100,65), new Vector(1,0,2.5)), 6)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienLeftFeeler = new Cylinder(1, new Ray(new Point(-1,100,65), new Vector(-1,0,2.5)), 6)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienEye = new Sphere(new Point(0,98,63.5), 2.8)
                .setEmission(new Color(230, 230, 230)).setMaterial(new Material().setKD(0.5).setKS(0.7).setShininess(150));
        Geometry alienPupil = new Sphere(new Point(0,95,64), 1)
                .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.2).setKS(0.9).setShininess(300));

        sceneTest.geometries.add(alienHead, alienBody, alienRightLeg, alienLeftLeg, alienRightHand, alienLeftHand, alienRightFeeler, alienLeftFeeler, alienEye, alienPupil);

        // ufo dome
        Point ufoDomeCenter = new Point(0,100,50);
        double ufoDomeRadius = 20;
        Color ufoDomeEmission = new Color(30, 30, 70);
        Material ufoDomeMaterial = new Material().setKD(0.1).setKS(0.7).setShininess(300).setKT(0.8).setKR(0.2);

        // ufo cylinder
        Ray ufoCylinderRay = new Ray(new Point(0,100,44), Vector.AXIS_Z);
        double ufoCylinderRadius = 22;
        double ufoCylinderHeight = 3;
        Color ufoCylinderEmission = new Color(192, 90, 0);
        Material ufoCylinderMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(100);

        // ufo disk (skirt)
        double topRadiusDisk = 18;
        double bottomRadiusDisk = 50;
        double heightTopDisk = 44;
        double heightBottomDisk = 28;
        double xCenterDisk = 0;
        double yCenterDisk = 100;
        int numberOfDots = 40;
        Color ufoDiskEmission = new Color(140, 0, 0);
        Material ufoDiskMaterial = new Material().setKD(0.7).setKS(0.2).setShininess(80);
        Point[] topCircleDots = new Point[numberOfDots];
        Point[] bottomCircleDots = new Point[numberOfDots];

        // circles on the ufo's dome
        Color circleColor = new Color(90,90,90);
        Vector circleNormal = Vector.AXIS_Z;
        Material circleMaterial = new Material().setKD(0.5);

        // glow spheres
        int numGlowSpheres = 16;
        double glowSphereRadius = 3;
        double glowSphereCircleRadius = 48;
        double zGlowSphere = 25;
        Color glowSphereEmission = new Color(255, 255, 150);
        Material glowSphereMaterial= new Material().setKD(0.1).setKS(0.5).setShininess(300);

        // ufo laser
        double laserRadius = 10;
        Ray laserRay = new Ray(new Point(0, 100, 28), new Vector(0, 0, -1));
        double laserHeight = 70;
        Color laserEmission = new Color(70, 70, 0);
        Material laserMaterial = new Material().setKT(1).setKD(0.1).setShininess(100);

        // ufo dome
        Geometry ufoDome = new Sphere(ufoDomeCenter, ufoDomeRadius)
                .setEmission(ufoDomeEmission)
                .setMaterial(ufoDomeMaterial);

        // ufo cylinder
        Geometry ufoCylinder = new Cylinder(ufoCylinderRadius, ufoCylinderRay, ufoCylinderHeight)
                .setEmission(ufoCylinderEmission)
                .setMaterial(ufoCylinderMaterial);

        // circles on the ufo's dome
        Geometry circle1 = new Circle(new Point(0,100,40), 28, circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);
        Geometry circle2 = new Circle(new Point(0,100,36), 36, circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);
        Geometry circle3 = new Circle(new Point(0,100,32), 45, circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);

        // ufo laser
        Geometry laser = new Cylinder(laserRadius, laserRay,laserHeight)
                .setEmission(laserEmission)
                .setMaterial(laserMaterial);

        // dots for creating the ufo's disk (skirt)
        for (int i = 0; i < numberOfDots; i++) {
            double angle = i * (2 * Math.PI / numberOfDots);
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            double xTop = topRadiusDisk * cosAngle + xCenterDisk;
            double yTop = topRadiusDisk * sinAngle + yCenterDisk;
            double xBottom = bottomRadiusDisk * cosAngle + xCenterDisk;
            double yBottom = bottomRadiusDisk * sinAngle + yCenterDisk;

            topCircleDots[i] = new Point(xTop, yTop, heightTopDisk);
            bottomCircleDots[i] = new Point(xBottom, yBottom, heightBottomDisk);
        }

        // ufo disk (skirt)
        for (int dot = 0; dot < numberOfDots; dot++) {
            int nextDot = (dot + 1) % numberOfDots;
            sceneTest.geometries.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot])
                            .setEmission(ufoDiskEmission)
                            .setMaterial(ufoDiskMaterial));
        }

        // glow spheres
        for (int i = 0; i < numGlowSpheres; i++) {
            double angle = i * (2 * Math.PI / numGlowSpheres);
            double xGlowSphere = glowSphereCircleRadius * Math.cos(angle);
            double yGlowSphere = glowSphereCircleRadius * Math.sin(angle) + 100;

            sceneTest.geometries.add(
                    new Sphere(new Point(xGlowSphere, yGlowSphere, zGlowSphere), glowSphereRadius)
                            .setEmission(glowSphereEmission)
                            .setMaterial(glowSphereMaterial)
            );
        }

        Geometry moon = new Sphere(new Point(0,100,-250), 220)
                .setEmission(new Color(90, 90, 90))
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setKR(0.1).setShininess(10));

        // laser
        sceneTest.geometries.add(ufoDome, ufoCylinder, circle1, circle2, circle3, moon, laser);

        sceneTest.lights.add(new SpotLight(
                new Color(700, 600, 200), new Point(0,100,28), Vector.AXIS_Z.scale(-1))
                .setKl(0.0001)
                .setKq(0.00005)
                .setNarrowBeam(25)
        );
        sceneTest.lights.add(new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)));
        sceneTest.lights.add(new DirectionalLight(new Color(50,50,50), new Vector(0,1,-1)));
        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26))
        );

        cameraBuilder
                .setLocation(new Point(0, -2000, 600)) //
                .setDirection(new Point(0,0,0), Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(700, 700) //
                .build() //
                .renderImage() //
                .writeToImage("zzz");
    }
}

/**
 * cameraBuilder
 *                 .setLocation(new Point(0, -2000, 600)) //
 *                 .setDirection(new Point(0,0,0), Vector.AXIS_Y) //
 *                 .setVpDistance(1000).setVpSize(200, 200) //
 *                 .setResolution(700, 700) //
 *                 .build() //
 *                 .renderImage() //
 *                 .writeToImage("zzz");
 */