package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public final class Helpers {
    
    public static BufferedImage read(String fileName) throws IOException {
        return ImageIO.read(new File(fileName));
    }
    
    public static void write(BufferedImage image, String fileName) throws IOException {      
        ImageIO.write(image, "jpg", new File(fileName));
    }
    
    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baStream);
        return baStream.toByteArray();
    }
    
    public static BufferedImage bytesToImage(byte[] rawBytes) throws IOException {
        InputStream imageStream = new ByteArrayInputStream(rawBytes);
        return ImageIO.read(imageStream);
    }
}
