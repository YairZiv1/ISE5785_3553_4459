package special;

import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import java.util.Random;

import geometries.*;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Testing FullImageTest
 * This test creates a scene with various geometries and lights to demonstrate the rendering capabilities of the
 * ray tracer.
 * The scene includes stars, an alien, a UFO, a moon, and various lighting effects.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class FullImageTest {
    /** Default constructor to satisfy JavaDoc generator */
    FullImageTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for the tests */
    Scene sceneTest = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(sceneTest, RayTracerType.SIMPLE);

    /**
     * Test method for testing the rendering of a complex scene with various geometries and lighting effects.
     */
    @Test
    void allEffects() {
        // stars
        Geometries stars = new Geometries();

        Random rand = new Random();
        int numStars = 500;
        Color starEmission = new Color( 200 , 200 , 200);
        Material starMaterial = new Material().setKD(0.5).setKT(1).setShininess(200);

        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 1800 - 900;   // between -900 and 900
            double y = rand.nextDouble() * 1000 + 6000;   // between 6000 and 7000
            double z = rand.nextDouble() * 1500 - 2500;   // between -2500 and -1000

            double radius = rand.nextDouble() * 2 + 1; // between 1 and 3
            radius += Math.pow(rand.nextDouble(), 10) * 3; // just a few that are very big

            stars.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starEmission)
                            .setMaterial(starMaterial)
            );
        }

        // moon
        Point moonCenter = new Point(0,100,-250);
        double moonRadius = 220;
        Color moonEmission = new Color(90, 90, 90);
        Material moonMaterial = new Material().setKD(0.7).setKS(0.1).setKR(0.1).setShininess(10);

        Geometry moon = new Sphere(moonCenter, moonRadius)
                .setEmission(moonEmission)
                .setMaterial(moonMaterial);

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
        Geometry alienRightFeeler = new Cylinder(1, new Ray(new Point(1,100,65), new Vector(1,0,2.5)), 4)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienLeftFeeler = new Cylinder(1, new Ray(new Point(-1,100,65), new Vector(-1,0,2.5)), 4)
                .setEmission(alienEmission).setMaterial(alienMaterial);
        Geometry alienEye = new Sphere(new Point(0,98,63.5), 2.8)
                .setEmission(new Color(230, 230, 230)).setMaterial(new Material().setKD(0.5).setKS(0.7).setShininess(150));
        Geometry alienPupil = new Sphere(new Point(0,95,64), 1)
                .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.2).setKS(0.9).setShininess(300));

        Geometries alien = new Geometries(
                alienHead, alienBody, alienRightLeg, alienLeftLeg, alienRightHand,
                alienLeftHand, alienRightFeeler, alienLeftFeeler, alienEye, alienPupil
        );

        // ufo dome
        Point ufoDomeCenter = new Point(0,100,50);
        double ufoDomeRadius = 20;
        Color ufoDomeEmission = new Color(30, 30, 70);
        Material ufoDomeMaterial = new Material().setKD(0.1).setKS(0.7).setShininess(300).setKT(0.8).setKR(0.2);

        Geometry ufoDome = new Sphere(ufoDomeCenter, ufoDomeRadius)
                .setEmission(ufoDomeEmission)
                .setMaterial(ufoDomeMaterial);

        // another ufo dome for the other ufos
        Geometry otherUfosDome = new Sphere(ufoDomeCenter, ufoDomeRadius)
                .setEmission(new Color(0, 0, 0))
                .setMaterial(new Material().setKD(0).setKS(3).setShininess(50).setKR(1));

        // ufo cylinder
        Ray ufoCylinderRay = new Ray(new Point(0,100,44), Vector.AXIS_Z);
        double ufoCylinderRadius = 22;
        double ufoCylinderHeight = 3;
        Color ufoCylinderEmission = new Color(192, 90, 0);
        Material ufoCylinderMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(100);

        Geometry ufoCylinder = new Cylinder(ufoCylinderRadius, ufoCylinderRay, ufoCylinderHeight)
                .setEmission(ufoCylinderEmission)
                .setMaterial(ufoCylinderMaterial);

        // ufo disk (skirt)
        Geometries ufoDisk = new Geometries();
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
        // creating ufo disk (skirt)
        for (int dot = 0; dot < numberOfDots; dot++) {
            int nextDot = (dot + 1) % numberOfDots;
            ufoDisk.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot])
                            .setEmission(ufoDiskEmission)
                            .setMaterial(ufoDiskMaterial)
            );
        }

        // circles on the ufo's dome
        Color circleColor = new Color(90,90,90);
        Vector circleNormal = Vector.AXIS_Z;
        Material circleMaterial = new Material().setKD(0.5);

        Geometry circle1 = new Circle(28, new Point(0,100,40), circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);
        Geometry circle2 = new Circle(36, new Point(0,100,36), circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);
        Geometry circle3 = new Circle(45, new Point(0,100,32), circleNormal)
                .setEmission(circleColor)
                .setMaterial(circleMaterial);

        // glow spheres
        Geometries ufoGlowSpheres = new Geometries();
        int numGlowSpheres = 16;
        double glowSphereRadius = 3;
        double glowSphereCircleRadius = 48;
        double zGlowSphere = 25.5;
        Color glowSphereEmission = new Color(255, 255, 150);
        Material glowSphereMaterial= new Material().setKD(0.1).setKS(0.5).setShininess(300);

        for (int i = 0; i < numGlowSpheres; i++) {
            double angle = i * (2 * Math.PI / numGlowSpheres);
            double xGlowSphere = glowSphereCircleRadius * Math.cos(angle);
            double yGlowSphere = glowSphereCircleRadius * Math.sin(angle) + 100;

            ufoGlowSpheres.add(
                    new Sphere(new Point(xGlowSphere, yGlowSphere, zGlowSphere), glowSphereRadius)
                            .setEmission(glowSphereEmission)
                            .setMaterial(glowSphereMaterial)
            );
        }

        Geometries ufo = new Geometries(ufoDome,
                ufoCylinder, ufoDisk, circle1, circle2, circle3, ufoGlowSpheres);

        Geometries otherUfos = new Geometries(otherUfosDome,
                ufoCylinder, ufoDisk, circle1, circle2, circle3, ufoGlowSpheres);

        // ufo laser
        double laserRadius = 10;
        Ray laserRay = new Ray(new Point(0, 100, 28), new Vector(0, 0, -1));
        double laserHeight = 70;
        Color laserEmission = new Color(70, 70, 0);
        Material laserMaterial = new Material().setKT(1).setKD(0.1).setShininess(100);

        Geometry laser = new Cylinder(laserRadius, laserRay,laserHeight)
                .setEmission(laserEmission)
                .setMaterial(laserMaterial);

        // adding stars and moon to the scene
        sceneTest.geometries.add(stars, moon);
        // adding the main alien and ufo and its laser to the scene
        sceneTest.geometries.add(alien, ufo);
        sceneTest.geometries.add(laser);
        sceneTest.lights.add(new SpotLight(
                new Color(700, 600, 200), new Point(0,100,28), Vector.AXIS_Z.scale(-1))
                .setKl(0.0001)
                .setKq(0.00005)
                .setNarrowBeam(25)
        );

        // adding other aliens and ufos to the scene
        sceneTest.geometries.add(otherUfos.move(new Vector(-45, -1700, 410)));
        sceneTest.geometries.add(otherUfos.move(new Vector(70, -1200, 250)));
        sceneTest.geometries.add(otherUfos.move(new Vector(-80, -450, 30)));
        sceneTest.geometries.add(otherUfos.move(new Vector(200, 270, -120)));
        sceneTest.geometries.add(otherUfos.move(new Vector(130, 340, -110)));
        sceneTest.geometries.add(otherUfos.move(new Vector(200, 470, -120)));
        sceneTest.geometries.add(otherUfos.move(new Vector(70, 500, -220)));
        sceneTest.geometries.add(otherUfos.move(new Vector(70, 1700, -320)));
        sceneTest.geometries.add(otherUfos.move(new Vector(-250, 1700, -310)));
        sceneTest.geometries.add(otherUfos.move(new Vector(-50, 2000, -300)));
        sceneTest.geometries.add(otherUfos.move(new Vector(320, 4000, -700)));

        sceneTest.lights.add(new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)));
        sceneTest.lights.add(new DirectionalLight(new Color(70,70,70), new Vector(0,1,-1)));

        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));

        cameraBuilder
                .setLocation(new Point(0, -2000, 600)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(700, 700) //
                .setAperture(10, 2160, 4) //
                .setMultithreading(-2) //
                .build() //
                .renderImage() //
                .writeToImage("UFOs in space");
    }
}