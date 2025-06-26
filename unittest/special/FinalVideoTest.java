package special;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.LightSource;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class generates a video of a UFO scene with stars, moon, alien, and UFOs.
 * It uses the Ray Tracer to render each frame and ffmpeg to compile the frames into a video.
 * @author Yair Ziv and Amitay Yosh'i
 */
public class FinalVideoTest {
    /** Default constructor to satisfy JavaDoc generator */
    FinalVideoTest() { /* to satisfy JavaDoc generator */ }

    /** Scene for the tests */
    Scene sceneTest = new Scene("Test scene");
    /** Camera builder for the tests */
    Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(sceneTest, RayTracerType.SIMPLE)
            .setLocation(new Point(0, -2000, 600)) //
            .setDirection(Point.ZERO, Vector.AXIS_Y) //
            .setVpDistance(1000).setVpSize(200, 200) //
            .enableBVH() //
            .setMultithreading(-2);

    /**
     * Test method for testing the rendering of a complex video with various geometries and lighting effects.
     */
    @Test
    void videoMaker() {
        // ------------------ stars ------------------
        Geometries stars = new Geometries();

        Random rand = new Random();
        int numStars = 500;
        Color starEmission = new Color( 200 , 200 , 200);
        Material starMaterial = new Material().setKD(0.5).setKT(1).setShininess(200);

        for (int i = 0; i < numStars; i++) {
            double x = rand.nextDouble() * 700 - 350;   // between -350 and 350
            double y = rand.nextDouble() * 500 + 500;   // between 500 and 1000
            double z = rand.nextDouble() * 500 - 450;   // between -500 and 0

            double radius = rand.nextDouble() * 0.3 + 0.5; // between 1 and 3
            radius += Math.pow(rand.nextDouble(), 10) * 0.5; // just a few that are very big

            stars.add(
                    new Sphere(new Point(x, y, z), radius)
                            .setEmission(starEmission)
                            .setMaterial(starMaterial)
            );
        }


        // ------------------ moon ------------------
        Point moonCenter = new Point(0,100,-250);
        double moonRadius = 220;
        Color moonEmission = new Color(90, 90, 90);
        Material moonMaterial = new Material().setKD(0.7).setKS(0.1).setKR(0.1).setShininess(10);

        Geometry moon = new Sphere(moonCenter, moonRadius)
                .setEmission(moonEmission)
                .setMaterial(moonMaterial);


        // ------------------ alien ------------------
        Color alienEmission = new Color(60, 192, 60);
        Material alienMaterial = new Material().setKD(0.3).setKS(0.6).setShininess(150).setKT(0.2);
        Point alienEyePosition = new Point(0,98,63.5);

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
        Geometry alienEye = new Sphere(alienEyePosition, 2.8)
                .setEmission(new Color(230, 230, 230)).setMaterial(new Material().setKD(0.5).setKS(0.7).setShininess(150));
        Geometry alienPupil = new Sphere(new Point(0,95,64), 1)
                .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.2).setKS(0.9).setShininess(300));

        Geometries alien = new Geometries(
                alienHead, alienBody, alienRightLeg, alienLeftLeg, alienRightHand,
                alienLeftHand, alienRightFeeler, alienLeftFeeler, alienEye, alienPupil
        );


        // ------------------ ufo dome ------------------
        Point ufoDomeCenter = new Point(0,100,50);
        double ufoDomeRadius = 20;
        Color blackUfoDomeEmission = new Color(0, 0, 0);
        Material blackUfoDomeMaterial = new Material().setKD(0).setKS(3).setShininess(50);
        Color transparentUfoDomeEmission = new Color(30, 30, 70);
        Material transparentUfoDomeMaterial = new Material().setKD(0.1).setKS(0.7).setShininess(300).setKT(0.8).setKR(0.2);

        Geometry ufoDome = new Sphere(ufoDomeCenter, ufoDomeRadius)
                .setEmission(blackUfoDomeEmission)
                .setMaterial(blackUfoDomeMaterial);


        // ------------------ ufo cylinder ------------------
        Ray ufoCylinderRay = new Ray(new Point(0,100,44), Vector.AXIS_Z);
        double ufoCylinderRadius = 22;
        double ufoCylinderHeight = 3;
        Color ufoCylinderEmission = new Color(192, 90, 0);
        Material ufoCylinderMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(100);

        Geometry ufoCylinder = new Cylinder(ufoCylinderRadius, ufoCylinderRay, ufoCylinderHeight)
                .setEmission(ufoCylinderEmission)
                .setMaterial(ufoCylinderMaterial);


        // ------------------ ufo disk (skirt) ------------------
        // two types of ufo disks (skirt) to create a more interesting look
        Geometries ufoDiskType1 = new Geometries();
        Geometries ufoDiskType2 = new Geometries();
        double topRadiusDisk = 18;
        double bottomRadiusDisk = 50;
        double heightTopDisk = 44;
        double heightBottomDisk = 28;
        double xCenterDisk = 0;
        double yCenterDisk = 100;
        int numberOfDots = 40;
        Color ufoDiskEmissionType1 = new Color(150, 0, 0);
        Color ufoDiskEmissionType2 = new Color(40, 40, 110);
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
        // creating the two types of ufo disk
        for (int dot = 0; dot < numberOfDots; dot++) {
            int nextDot = (dot + 1) % numberOfDots;
            ufoDiskType1.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot]
                    ).setEmission(ufoDiskEmissionType1)
                            .setMaterial(ufoDiskMaterial)
            );
            ufoDiskType2.add(
                    new Polygon(
                            bottomCircleDots[dot],
                            bottomCircleDots[nextDot],
                            topCircleDots[nextDot],
                            topCircleDots[dot]
                    ).setEmission(ufoDiskEmissionType2)
                            .setMaterial(ufoDiskMaterial)
            );
        }


        // ------------------ circles for the disk ------------------
        Vector circleNormal = Vector.AXIS_Z;
        Material circleMaterial = new Material().setKD(0.5);

        Geometry circle1 = new Circle(28, new Point(0,100,40), circleNormal)
                .setEmission(new Color(90,90,90))
                .setMaterial(circleMaterial);
        Geometry circle2 = new Circle(36, new Point(0,100,36), circleNormal)
                .setEmission(new Color(90,90,90))
                .setMaterial(circleMaterial);
        Geometry circle3 = new Circle(45, new Point(0,100,32), circleNormal)
                .setEmission(new Color(90,90,90))
                .setMaterial(circleMaterial);


        // ------------------ ufo glow spheres ------------------
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


        // ------------------ variables for the ufo laser ------------------
        Point laserPosition = new Point(0, 100, 28);
        Ray laserRay = new Ray(laserPosition, new Vector(0, 0, -1));
        double laserHeight = 700;
        double laserRadius = 10;
        Color laserEmission = new Color(70, 70, 0);
        Material laserMaterial = new Material().setKT(1).setKD(0.1).setShininess(100);
        Color laserSpotEmission = new Color(525, 450, 150);


        // set the ambient light for the scene before the loop
        sceneTest.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));


        final int numFrames = 200; // number of frames to render
        final int frames1 = 60; // frames for the first part of the video
        final int frames2 = 10; // frames for the second part of the video
        final int frames3 = 40; // frames for the third part of the video
        final int frames4 = 10; // frames for the fourth part of the video
        final int frames5 = 80; // frames for the fifth part of the video

        final Vector upHover = new Vector(0, 0, 30); // represents the upward movement of the UFOs in the third part

        // create an array to hold the geometries in the scene for each frame
        Geometries[] frameScenes = new Geometries[numFrames];


        // loop through each frame to create the scene
        for (int frameNumber = 0; frameNumber < numFrames; ++frameNumber) {
            // initialize the scene for the current frame
            frameScenes[frameNumber] = new Geometries();

            // adding stars and moon to the scene
            frameScenes[frameNumber].add(stars.getGeometries());
            frameScenes[frameNumber].add(moon);

            // list to hold the lights for the current scene
            List<LightSource> frameLights = new LinkedList<>(
                    List.of(
                            new DirectionalLight(new Color(100,100,100), new Vector(-8,-10,-10)),
                            new DirectionalLight(new Color(70,70,70), new Vector(0,1,-1))
                    )
            );

            // create two types of UFOs in different colors for different lines
            Geometries ufoType1 = new Geometries(ufoCylinder, circle1, circle2, circle3);
            ufoType1.add(ufoDiskType1.getGeometries());
            ufoType1.add(ufoGlowSpheres.getGeometries());
            ufoType1.add(alien.getGeometries());

            Geometries ufoType2 = new Geometries(ufoCylinder, circle1, circle2, circle3);
            ufoType2.add(ufoDiskType2.getGeometries());
            ufoType2.add(ufoGlowSpheres.getGeometries());
            ufoType2.add(alien.getGeometries());

            // for the third part - ufo dome changes its color and material, and aliens appear slowly
            // the laser is added to the UFOs
            if ((frames1 + frames2) <= frameNumber && frameNumber < (frames1 + frames2 + frames3)) {
                // progress factor for the third part
                double pFactor = (double) (frameNumber - (frames1 + frames2 - 1)) / (double) frames3;

                ufoDome
                        .setEmission(transparentUfoDomeEmission.scale(pFactor))
                        .setMaterial(new Material()
                                .setKD(0.1 * pFactor)
                                .setKS(3 - 2.3 * pFactor)
                                .setShininess(50 + (int) (250 * pFactor))
                                .setKT(0.8 * pFactor)
                                .setKR(0.2 * pFactor));

                Geometry laser = new Cylinder(laserRadius, laserRay,laserHeight)
                        .setEmission(laserEmission)
                        .setMaterial(laserMaterial);
                ufoType1.add(laser);
                ufoType2.add(laser);
            }
            // for the fourth and fifth parts - ufo dome is transparent
            else if ((frames1 + frames2 + frames3) <= frameNumber) {
                ufoDome
                        .setEmission(transparentUfoDomeEmission)
                        .setMaterial(transparentUfoDomeMaterial);

                // for the fourth part - laser is added to the UFOs and its radius decreases
                if (frameNumber < (frames1 + frames2 + frames3 + frames4)) {
                    double newLaserRadius = (double) (frames1 + frames2 + frames3 + frames4 - frameNumber)
                            * laserRadius / frames4;

                    Geometry laser = new Cylinder(newLaserRadius, laserRay, laserHeight)
                            .setEmission(laserEmission)
                            .setMaterial(laserMaterial);
                    ufoType1.add(laser);
                    ufoType2.add(laser);
                }
            }

            // add the ufo dome to both types of UFOs
            ufoType1.add(ufoDome);
            ufoType2.add(ufoDome);

            // Vector offsets for the different UFOs to create an arrow shape
            Vector positionOffset00 = new Vector(0, -150, 20);
            Vector positionOffset10 = new Vector(-75, 0, 0);
            Vector positionOffset11 = new Vector(75, 0, 0);
            Vector positionOffset20 = new Vector(-150, 150, -20);
            Vector positionOffset21 = new Vector(0, 150, -20);
            Vector positionOffset22 = new Vector(150, 150, -20);

            // create the different UFOs from the different types and apply the correct position offsets
            Geometries ufo00 = ufoType1.move(positionOffset00);
            Geometries ufo10 = ufoType2.move(positionOffset10);
            Geometries ufo11 = ufoType2.move(positionOffset11);
            Geometries ufo20 = ufoType1.move(positionOffset20);
            Geometries ufo21 = ufoType1.move(positionOffset21);
            Geometries ufo22 = ufoType1.move(positionOffset22);

            // this point is used to follow the alien's eye position (in ufo00) to find the correct focal distance
            Point alienEye00 = alienEyePosition.add(positionOffset00);

            // first part - UFOs are coming and making an arrow shape
            if (frameNumber < frames1) {
                // progress factor for the first part
                double pFactor = (frames1 - (double) frameNumber) / frames1;

                frameScenes[frameNumber].add(ufo00.move(new Vector(0, 500, 300).scale(pFactor)).getGeometries());
                frameScenes[frameNumber].add(ufo10.move(new Vector(-500, 0, 200).scale(pFactor)).getGeometries());
                frameScenes[frameNumber].add(ufo11.move(new Vector(500, 0, 200).scale(pFactor)).getGeometries());
                frameScenes[frameNumber].add(ufo20.move(new Vector(-500, 0, 200).scale(pFactor)).getGeometries());
                frameScenes[frameNumber].add(ufo21.move(new Vector(0, 500, 100).scale(pFactor)).getGeometries());
                frameScenes[frameNumber].add(ufo22.move(new Vector(500, 0, 200).scale(pFactor)).getGeometries());

                // move the alien's eye position to follow the UFOs
                alienEye00 = alienEye00.add(new Vector(0, 500, 300).scale(pFactor));
            }
            // second part - UFOs are waiting in place
            else if (frameNumber < (frames1 + frames2)) {
                frameScenes[frameNumber].add(ufo00.getGeometries());
                frameScenes[frameNumber].add(ufo10.getGeometries());
                frameScenes[frameNumber].add(ufo11.getGeometries());
                frameScenes[frameNumber].add(ufo20.getGeometries());
                frameScenes[frameNumber].add(ufo21.getGeometries());
                frameScenes[frameNumber].add(ufo22.getGeometries());
            }
            // third part - UFOs going up and lights on
            else if (frameNumber < (frames1 + frames2 + frames3)) {
                // progress factor for the third part
                double pFactor = ((double) frameNumber - (frames1 + frames2 - 1)) / frames3;
                Vector moveVector = upHover.scale(pFactor);
                Point lightPosition = laserPosition.add(moveVector);

                frameScenes[frameNumber].add(ufo00.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo10.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo11.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo20.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo21.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo22.move(moveVector).getGeometries());

                // move the alien's eye position to follow the UFOs
                alienEye00 = alienEye00.add(moveVector);

                frameLights.addAll(
                        List.of(
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset00),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset10),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset11),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset20),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset21),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset22),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25)
                        )
                );
            }
            else if (frameNumber < (frames1 + frames2 + frames3 + frames4)) {
                Point lightPosition = laserPosition.add(upHover);

                frameScenes[frameNumber].add(ufo00.move(upHover).getGeometries());
                frameScenes[frameNumber].add(ufo10.move(upHover).getGeometries());
                frameScenes[frameNumber].add(ufo11.move(upHover).getGeometries());
                frameScenes[frameNumber].add(ufo20.move(upHover).getGeometries());
                frameScenes[frameNumber].add(ufo21.move(upHover).getGeometries());
                frameScenes[frameNumber].add(ufo22.move(upHover).getGeometries());

                // move the alien's eye position to follow the UFOs
                alienEye00 = alienEye00.add(upHover);

                frameLights.addAll(
                        List.of(
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset00),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset10),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset11),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset20),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset21),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25),
                                new SpotLight(laserSpotEmission, lightPosition.add(positionOffset22),
                                        Vector.AXIS_Z.scale(-1)).setKl(0.0001).setKq(0.00005).setNarrowBeam(25)
                        )
                );
            }
            // fifth part - UFOs moving towards the camera
            else {
                // progress factor for the fifth part
                double pFactor = ((double) frameNumber - (frames1 + frames2 + frames3 + frames4 - 1)) / frames5;
                Vector moveVector = upHover.add(new Vector(0, -1942, 484.5).scale(pFactor));

                frameScenes[frameNumber].add(ufo00.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo10.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo11.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo20.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo21.move(moveVector).getGeometries());
                frameScenes[frameNumber].add(ufo22.move(moveVector).getGeometries());

                // move the alien's eye position to follow the UFOs
                alienEye00 = alienEye00.add(moveVector);
            }

            // set the scene's geometries and lights according to the current frame
            sceneTest.setGeometries(frameScenes[frameNumber]);
            sceneTest.setLights(frameLights);

            // calculate the focal distance based on the alien's eye position
            double focalDistance = new Point(0, -2000, 600).distance(alienEye00);

            // render the image for the current frame
            cameraBuilder
                    .setResolution(1000, 1000) //
                    .setAperture(20, focalDistance, 9) //
                    .build() //
                    .renderImage() //
                    .writeToImage("frame" + String.format("%03d", frameNumber));
        }

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-y",
                "-loglevel", "error", // print just errors
                "-start_number", "1", // print just errors
                "-framerate", "20", // 20 FPS
                "-i", "images/frame%03d.png", // input frames
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                "images/UFOsVideo.mp4" // output video
        );

        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("video created successfully!");
            } else {
                System.err.println("ffmpeg failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
