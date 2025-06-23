package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.Random;

/**
 * Testing bounding box in rendering (CBR and BVH)
 * @author Yair Ziv and Amitay Yosh'i
 */
public class BoundingBoxTest {
    /** Default constructor to satisfy JavaDoc generator */
    BoundingBoxTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for the tests */
    Scene sceneTest = new Scene("Test scene");
    /** Camera builder for the tests */
    Camera.Builder cameraBuilder = Camera.getBuilder() //
            .setRayTracer(sceneTest, RayTracerType.SIMPLE) //
            .setLocation(new Point(0, -2000, 600)) //
            .setDirection(Point.ZERO, Vector.AXIS_Y) //
            .setVpDistance(1000).setVpSize(200, 200) //
            .setResolution(1000, 1000);

    // stars
    /** Random number generator for generating stars */
    Random rand = new Random();

    /** Number of stars for the tests */
    int numStars = 1000;
    /** Emission color for the stars */
    Color starEmission = new Color( 200 , 200 , 200);
    /** Material for the stars */
    Material starMaterial = new Material().setKD(0.5).setKT(1).setShininess(200);

    // moon
    /** Center point for the tests */
    Point moonCenter = new Point(0,100,-250);
    /** Radius for the tests */
    double moonRadius = 220;
    /** Emission color for the tests */
    Color moonEmission = new Color(90, 90, 90);
    /** Material for the tests */
    Material moonMaterial = new Material().setKD(0.7).setKS(0.1).setKR(0.1).setShininess(10);

    /** Moon geometry for the scene */
    Geometry moon = new Sphere(moonCenter, moonRadius)
            .setEmission(moonEmission)
            .setMaterial(moonMaterial);

    // alien
    /** Color for the tests */
    Color alienEmission = new Color(60, 192, 60);
    /** Material for the tests */
    Material alienMaterial = new Material().setKD(0.3).setKS(0.6).setShininess(150).setKT(0.2);

    /** Alien head for the scene */
    Geometry alienHead = new Sphere(new Point(0,100,63), 4)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** * Alien body for the scene */
    Geometry alienBody = new Cylinder(1, new Ray(new Point(0,100,59), Vector.AXIS_Z.scale(-1)), 5)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien right leg for the scene */
    Geometry alienRightLeg = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(1,0,-2)), 8)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien left leg for the scene */
    Geometry alienLeftLeg = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(-1,0,-2)), 8)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien right hand for the scene */
    Geometry alienRightHand = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(1,0,1)), 8)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien left hand for the scene */
    Geometry alienLeftHand = new Cylinder(1, new Ray(new Point(0,100,54), new Vector(-1,0,1)), 8)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien feelers for the scene */
    Geometry alienRightFeeler = new Cylinder(1, new Ray(new Point(1,100,65), new Vector(1,0,2.5)), 4)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien left feeler for the scene */
    Geometry alienLeftFeeler = new Cylinder(1, new Ray(new Point(-1,100,65), new Vector(-1,0,2.5)), 4)
            .setEmission(alienEmission).setMaterial(alienMaterial);
    /** Alien eye and pupil for the scene */
    Geometry alienEye = new Sphere(new Point(0,98,63.5), 2.8)
            .setEmission(new Color(230, 230, 230)).setMaterial(new Material().setKD(0.5).setKS(0.7).setShininess(150));
    /** Alien pupil for the scene */
    Geometry alienPupil = new Sphere(new Point(0,95,64), 1)
            .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.2).setKS(0.9).setShininess(300));

    // ufo dome
    /** Center point for the tests */
    Point ufoDomeCenter = new Point(0,100,50);
    /** Radius for the tests */
    double ufoDomeRadius = 20;
    /** Emission color for the tests */
    Color ufoDomeEmission = new Color(30, 30, 70);
    /** Material for the tests */
    Material ufoDomeMaterial = new Material().setKD(0.1).setKS(0.7).setShininess(300).setKT(0.8).setKR(0.2);

    /** UFO dome geometry for the scene */
    Geometry ufoDome = new Sphere(ufoDomeCenter, ufoDomeRadius)
            .setEmission(ufoDomeEmission)
            .setMaterial(ufoDomeMaterial);

    // ufo cylinder
    /** Ray for the tests */
    Ray ufoCylinderRay = new Ray(new Point(0,100,44), Vector.AXIS_Z);
    /** Radius for the tests */
    double ufoCylinderRadius = 22;
    /** Height for the tests */
    double ufoCylinderHeight = 3;
    /** Emission color for the tests */
    Color ufoCylinderEmission = new Color(192, 90, 0);
    /** Material for the tests */
    Material ufoCylinderMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(100);

    /** UFO cylinder geometry for the scene */
    Geometry ufoCylinder = new Cylinder(ufoCylinderRadius, ufoCylinderRay, ufoCylinderHeight)
            .setEmission(ufoCylinderEmission)
            .setMaterial(ufoCylinderMaterial);

    // ufo disk (skirt)
    /** Radius for the tests */
    double topRadiusDisk = 18;
    /** Radius for the tests */
    double bottomRadiusDisk = 50;
    /** Height for the tests */
    double heightTopDisk = 44;
    /** Height for the tests */
    double heightBottomDisk = 28;
    /** Center coordinate for the tests */
    double xCenterDisk = 0;
    /** Center coordinate for the tests */
    double yCenterDisk = 100;
    /** Number of dots for the tests */
    int numberOfDots = 40;
    /** Emission color for the tests */
    Color ufoDiskEmission = new Color(140, 0, 0);
    /** Material for the tests */
    Material ufoDiskMaterial = new Material().setKD(0.7).setKS(0.2).setShininess(80);
    /** Points for the top circle of the ufo disk */
    Point[] topCircleDots = new Point[numberOfDots];
    /** Points for the bottom circle of the ufo disk */
    Point[] bottomCircleDots = new Point[numberOfDots];

    // circles on the ufo's dome
    /** Color for the circles on the ufo's dome */
    Color circleColor = new Color(90,90,90);
    /** Normal vector for the circles on the ufo's dome */
    Vector circleNormal = Vector.AXIS_Z;
    /** Material for the circles on the ufo's dome */
    Material circleMaterial = new Material().setKD(0.5);

    /** First circle on the ufo's dome for the scene */
    Geometry circle1 = new Circle(28, new Point(0,100,40), circleNormal)
            .setEmission(circleColor)
            .setMaterial(circleMaterial);
    /** Second circle on the ufo's dome for the scene */
    Geometry circle2 = new Circle(36, new Point(0,100,36), circleNormal)
            .setEmission(circleColor)
            .setMaterial(circleMaterial);
    /** Third circle on the ufo's dome for the scene */
    Geometry circle3 = new Circle(45, new Point(0,100,32), circleNormal)
            .setEmission(circleColor)
            .setMaterial(circleMaterial);

    // glow spheres
    /** Number of glow spheres around the ufo */
    int numGlowSpheres = 16;
    /** Radius of the glow spheres */
    double glowSphereRadius = 3;
    /** Radius of the circle on which the glow spheres are placed */
    double glowSphereCircleRadius = 48;
    /** Z coordinate for the glow spheres */
    double zGlowSphere = 25.5;
    /** Material for the glow spheres */
    Material transperentMaterial = new Material().setKT(1).setKD(0.1).setShininess(100);

    // ufo laser
    /** Radius of the laser beam */
    double laserRadius = 10;
    /** Ray for the laser beam */
    Ray laserRay = new Ray(new Point(0, 100, 28), new Vector(0, 0, -1));
    /** Height of the laser beam */
    double laserHeight = 70;

    /** UFO laser geometry for the scene */
    Geometry laser = new Cylinder(laserRadius, laserRay,laserHeight)
            .setEmission(new Color(70, 70, 0))
            .setMaterial(transperentMaterial);

    /**
     * Test for rendering a scene with Conservative Bounding Region (CBR).
     */
    @Test
    void cBRTest() {
        // creating stars
        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 1800 - 900;   // between -900 and 900
            double y = rand.nextDouble() * 1000 + 6000;   // between 6000 and 7000
            double z = rand.nextDouble() * 1500 - 2500;   // between -2500 and -1000

            double radius = rand.nextDouble() * 2 + 1; // between 1 and 3

            sceneTest.geometries.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starEmission)
                            .setMaterial(starMaterial)
            );
        }

        sceneTest.geometries.add(moon);

        sceneTest.geometries.add(
                alienHead, alienBody, alienRightLeg, alienLeftLeg, alienRightHand,
                alienLeftHand, alienRightFeeler, alienLeftFeeler, alienEye, alienPupil
        );

        sceneTest.geometries.add(ufoDome);

        sceneTest.geometries.add(ufoCylinder);

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
            sceneTest.geometries.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot])
                            .setEmission(ufoDiskEmission)
                            .setMaterial(ufoDiskMaterial)
            );
        }

        sceneTest.geometries.add(circle1, circle2, circle3);

        // creating glow spheres around the ufo
        for (int i = 0; i < numGlowSpheres; i++) {
            double angle = i * (2 * Math.PI / numGlowSpheres);
            double xGlowSphere = glowSphereCircleRadius * Math.cos(angle);
            double yGlowSphere = glowSphereCircleRadius * Math.sin(angle) + 100;

            sceneTest.geometries.add(
                    new Sphere(new Point(xGlowSphere, yGlowSphere, zGlowSphere), glowSphereRadius)
                            .setEmission(new Color(100, 100, 60))
                            .setMaterial(transperentMaterial)
            );
            sceneTest.lights.add(new PointLight(
                    new Color(100, 100, 60), new Point(xGlowSphere, yGlowSphere, zGlowSphere))
                    .setKl(0.1)
                    .setKq(0.005)
            );
        }

        sceneTest.geometries.add(laser);

        sceneTest.lights.add(new SpotLight(
                new Color(700, 600, 200), new Point(0,100,28), Vector.AXIS_Z.scale(-1))
                .setKl(0.0001)
                .setKq(0.00005)
                .setNarrowBeam(25)
        );
        sceneTest.lights.add(new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)));
        sceneTest.lights.add(new DirectionalLight(new Color(70,70,70), new Vector(0,1,-1)));

        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));

        cameraBuilder
                .enableCBR() //
                .build() //
                .renderImage() //
                .writeToImage("UFOs in space with CBR");
    }

    /**
     * Test for rendering a scene with a manual Bounding Volume Hierarchy (BVH).
     */
    @Test
    void manualBVHTest() {
        // creating stars
        Geometries stars = new Geometries();
        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 1800 - 900;   // between -900 and 900
            double y = rand.nextDouble() * 1000 + 6000;   // between 6000 and 7000
            double z = rand.nextDouble() * 1500 - 2500;   // between -2500 and -1000

            double radius = rand.nextDouble() * 2 + 1; // between 1 and 3

            stars.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starEmission)
                            .setMaterial(starMaterial)
                            .setBoundingBox()
            );
        }
        stars.setBoundingBox();

        moon.setBoundingBox();

        Geometries alien = new Geometries(alienHead.setBoundingBox(), alienBody.setBoundingBox(),
                alienRightLeg.setBoundingBox(), alienLeftLeg.setBoundingBox(),
                alienRightHand.setBoundingBox(), alienLeftHand.setBoundingBox(),
                alienRightFeeler.setBoundingBox(), alienLeftFeeler.setBoundingBox(),
                alienEye.setBoundingBox(), alienPupil.setBoundingBox());
        alien.setBoundingBox();

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
        Geometries ufoDisk = new Geometries();
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
                            .setBoundingBox()
            );
        }

        // creating glow spheres around the ufo
        Geometries glowSpheres = new Geometries();
        for (int i = 0; i < numGlowSpheres; i++) {
            double angle = i * (2 * Math.PI / numGlowSpheres);
            double xGlowSphere = glowSphereCircleRadius * Math.cos(angle);
            double yGlowSphere = glowSphereCircleRadius * Math.sin(angle) + 100;

            glowSpheres.add(
                    new Sphere(new Point(xGlowSphere, yGlowSphere, zGlowSphere), glowSphereRadius)
                            .setEmission(new Color(100, 100, 60))
                            .setMaterial(transperentMaterial)
                            .setBoundingBox()
            );
            sceneTest.lights.add(new PointLight(
                    new Color(100, 100, 60), new Point(xGlowSphere, yGlowSphere, zGlowSphere))
                    .setKl(0.1)
                    .setKq(0.005)
            );
        }

        Geometries ufo = new Geometries(ufoDome.setBoundingBox(), ufoCylinder.setBoundingBox(),
                circle1.setBoundingBox(), circle2.setBoundingBox(), circle3.setBoundingBox(),
                ufoDisk.setBoundingBox(), glowSpheres.setBoundingBox());
        ufo.setBoundingBox();

        laser.setBoundingBox();

        Geometries mainGeometries = new Geometries(alien, ufo, laser);
        mainGeometries.setBoundingBox();

        sceneTest.geometries.add(stars, moon, mainGeometries);
        sceneTest.geometries.setBoundingBox();

        sceneTest.lights.add(new SpotLight(
                new Color(700, 600, 200), new Point(0,100,28), Vector.AXIS_Z.scale(-1))
                .setKl(0.0001)
                .setKq(0.00005)
                .setNarrowBeam(25)
        );
        sceneTest.lights.add(new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)));
        sceneTest.lights.add(new DirectionalLight(new Color(70,70,70), new Vector(0,1,-1)));

        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));

        cameraBuilder
                .build() //
                .renderImage() //
                .writeToImage("UFOs in space with manual BVH");
    }

    /**
     * Test for rendering a scene with an automatic Bounding Volume Hierarchy (BVH).
     */
    @Test
    void automaticBVHTest() {
        // creating stars
        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 1800 - 900;   // between -900 and 900
            double y = rand.nextDouble() * 1000 + 6000;   // between 6000 and 7000
            double z = rand.nextDouble() * 1500 - 2500;   // between -2500 and -1000

            double radius = rand.nextDouble() * 2 + 1; // between 1 and 3

            sceneTest.geometries.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starEmission)
                            .setMaterial(starMaterial)
            );
        }

        sceneTest.geometries.add(moon);

        sceneTest.geometries.add(
                alienHead, alienBody, alienRightLeg, alienLeftLeg, alienRightHand,
                alienLeftHand, alienRightFeeler, alienLeftFeeler, alienEye, alienPupil
        );

        sceneTest.geometries.add(ufoDome);

        sceneTest.geometries.add(ufoCylinder);

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
            sceneTest.geometries.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot])
                            .setEmission(ufoDiskEmission)
                            .setMaterial(ufoDiskMaterial)
            );
        }

        sceneTest.geometries.add(circle1, circle2, circle3);

        // creating glow spheres around the ufo
        for (int i = 0; i < numGlowSpheres; i++) {
            double angle = i * (2 * Math.PI / numGlowSpheres);
            double xGlowSphere = glowSphereCircleRadius * Math.cos(angle);
            double yGlowSphere = glowSphereCircleRadius * Math.sin(angle) + 100;

            sceneTest.geometries.add(
                    new Sphere(new Point(xGlowSphere, yGlowSphere, zGlowSphere), glowSphereRadius)
                            .setEmission(new Color(100, 100, 60))
                            .setMaterial(transperentMaterial)
            );
            sceneTest.lights.add(new PointLight(
                    new Color(100, 100, 60), new Point(xGlowSphere, yGlowSphere, zGlowSphere))
                    .setKl(0.1)
                    .setKq(0.005)
            );
        }

        sceneTest.geometries.add(laser);

        sceneTest.lights.add(new SpotLight(
                new Color(700, 600, 200), new Point(0,100,28), Vector.AXIS_Z.scale(-1))
                .setKl(0.0001)
                .setKq(0.00005)
                .setNarrowBeam(25)
        );
        sceneTest.lights.add(new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)));
        sceneTest.lights.add(new DirectionalLight(new Color(70,70,70), new Vector(0,1,-1)));

        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));

        cameraBuilder
                .enableBVH() //
                .build() //
                .renderImage() //
                .writeToImage("UFOs in space with automatic BVH");
    }
}
