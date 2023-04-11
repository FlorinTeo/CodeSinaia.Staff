package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import common.Helpers;
import common.MsgImage;

class MsgImage_tests {

    @Test
    void test_read_write() throws IOException, ClassNotFoundException {
        BufferedImage image1 = Helpers.readImage("data/shelter.jpg");
        BufferedImage image2 = Helpers.readImage("data/flower.jpg");
        MsgImage msgImage1 = new MsgImage(image1);
        MsgImage msgImage2 = new MsgImage(image2);
        
        // serialize images into a stream file
        FileOutputStream fos = new FileOutputStream("data/temp_stream.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(msgImage1);
        oos.writeObject(msgImage2);
        oos.close();
        
        // deserialize images from the stream file and verify they have same sizes and bytes
        FileInputStream fis = new FileInputStream("data/temp_stream.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        MsgImage msgImage3 = (MsgImage)ois.readObject();
        assertNotNull(msgImage3);
        BufferedImage image3 = msgImage3.getImage();
        assertEquals(image1.getHeight(), image3.getHeight());
        assertEquals(image1.getWidth(), image3.getWidth());

        MsgImage msgImage4 = (MsgImage)ois.readObject();
        assertNotNull(msgImage4);
        BufferedImage image4 = msgImage4.getImage();
        assertEquals(image2.getHeight(), image4.getHeight());
        assertEquals(image2.getWidth(), image4.getWidth());
        
        ois.close();
        
        Files.deleteIfExists(Paths.get("data/temp_stream.ser"));
    }

}
