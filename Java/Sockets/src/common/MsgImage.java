package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class MsgImage implements Serializable {
    private static final long serialVersionUID = 1L;
    private BufferedImage _image;
    
    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baStream);
        return baStream.toByteArray();
    }
    
    public static BufferedImage bytesToImage(byte[] rawBytes) throws IOException {
        InputStream imageStream = new ByteArrayInputStream(rawBytes);
        return ImageIO.read(imageStream);
    }
    
    public MsgImage(BufferedImage image) throws IOException {
        _image = image;
    }
    
    public BufferedImage getImage() {
        return _image;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] imgBytes = imageToBytes(_image);
        out.writeObject(imgBytes);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        byte[] imgBytes = (byte[])in.readObject();
        _image = bytesToImage(imgBytes);
    }
}
