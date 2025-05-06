package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;

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
        for (int i = 0; i < imageWriter.nX(); i++) {
            for (int j = 0; j < imageWriter.nY(); j++) {
                // Every 50 pixels, lengthwise and widthwise, draw a red grid line
                if(i % 50 == 0 || j % 50 == 0)
                    imageWriter.writePixel(i, j, new Color(java.awt.Color.RED));
                // All other pixels are yellow
                else
                    imageWriter.writePixel(i, j, new Color(java.awt.Color.YELLOW));
            }
        }
        // Write the image to a file
        imageWriter.writeToImage("yellow with red grid");
    }
}