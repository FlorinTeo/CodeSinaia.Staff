package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import common.Helpers;

class Helpers_tests {

    @Test
    void test_image_ops() throws IOException {
        Path tempImgPath = Paths.get("data/temp_image.jpg");
        Files.deleteIfExists(tempImgPath);

        // read image from file, convert it to raw bytes
        // then back into image and write it to a second file.
        BufferedImage image = Helpers.read("data/shelter.jpg");
        assertNotNull(image);
        byte[] imgBytes = Helpers.imageToBytes(image);
        assertNotNull(imgBytes);
        BufferedImage image2 = Helpers.bytesToImage(imgBytes);
        assertNotNull(image2);        
        Helpers.write(image2, "data/temp_image.jpg");
        
        // read the newly created image from file,
        // and check it has the same size as the original one.
        // Bytes won't be the same since these are compressed jpgs
        BufferedImage image3 = Helpers.read("data/temp_image.jpg");
        assertNotNull(image3);
        assertEquals(image.getHeight(), image3.getHeight());
        assertEquals(image.getWidth(), image3.getWidth());
        
        // cleanup
        Files.deleteIfExists(tempImgPath);
    }

}
