package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing ImageWriter Class
 * @author Yair Ziv and Amitay Yosh'i.
 */
class ImageWriterTest {

    /**
     * Test method for {@link ImageWriter#writeToImage(String)}.
     */
    @Test
    void testWriteToImage() {
        // TC01: Yellow 800X500 pixels picture with red grid lines.
        // Create an ImageWriter with a width of 800 and height of 500
        ImageWriter imageWriter = new ImageWriter(800,500);

        // Loop through all pixels and set their color
        for (int i = 0; i < imageWriter.nX(); i++)
            for (int j = 0; j < imageWriter.nY(); j++)
                imageWriter.writePixel(i, j, new Color(YELLOW));

        // Loop that go through the rows and set the grid color
        for (int i = 0; i < imageWriter.nX(); i++)
            for (int j = 0; j < imageWriter.nY(); j+=50)
                imageWriter.writePixel(i, j, new Color(RED));

        // Loop that go through the columns and set the grid color
        for (int i = 0; i < imageWriter.nX(); i+=50)
            for (int j = 0; j < imageWriter.nY(); j++)
                imageWriter.writePixel(i, j, new Color(RED));

        // Write the image to a file
        imageWriter.writeToImage("yellow with red grid");
    }
}