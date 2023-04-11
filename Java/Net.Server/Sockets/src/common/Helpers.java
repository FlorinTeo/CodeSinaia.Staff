package common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public final class Helpers {
    
    public static BufferedImage readImage(String fileName) throws IOException {
        return ImageIO.read(new File(fileName));
    }
    
    public static void writeImage(BufferedImage image, String fileName) throws IOException {      
        ImageIO.write(image, "jpg", new File(fileName));
    }
    
    public static void sendImage(BufferedImage image, Socket socket) throws IOException {
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        MsgImage message = new MsgImage(image);
        outStream.writeObject(message);
    }
    
    public static BufferedImage receiveImage(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        MsgImage message = (MsgImage) inStream.readObject();
        BufferedImage image = message.getImage();
        return image;
    }
}
