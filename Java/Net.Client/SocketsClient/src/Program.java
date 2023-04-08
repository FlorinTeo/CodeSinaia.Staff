import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Program {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5015;
    
    private static void interactive() throws IOException {
        Scanner console = new Scanner(System.in);
        
        do {
            System.out.print("Message?> ");
            String message = console.nextLine();
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
            
            System.out.printf("Sending '%s' to %s/%d ... ", message, _IP, _PORT);
            Socket socket = new Socket(_IP, _PORT);
            OutputStream rawStream = socket.getOutputStream();
            DataOutputStream dataStream = new DataOutputStream(rawStream);
            dataStream.writeUTF(message);
            socket.close();
            System.out.println("DONE");

        } while(true);
        
        console.close();
    }
    
    private static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baStream);
        return baStream.toByteArray();
    }
    
    private static BufferedImage bytesToImage(byte[] rawBytes) throws IOException {
        InputStream imageStream = new ByteArrayInputStream(rawBytes);
        return ImageIO.read(imageStream);
    }

    private static void imgExchange() throws IOException, ClassNotFoundException {
        // Read image from file in byte[]
        String inFile = "shelter.jpg";
        BufferedImage inImg = ImageIO.read(new File(inFile));
        
        // Open socket to server and get its out/in streams
        Socket socket = new Socket(_IP, _PORT);
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        
        // Write byte[] to server
        MsgImage outMsg = new MsgImage(inImg);
        outStream.writeObject(outMsg);
        
        // Read answer from server in byte[]
        MsgImage inMsg = (MsgImage) inStream.readObject();
        BufferedImage outImg = inMsg.getImage();
        
        // Close streams & socket
        inStream.close();
        outStream.close();
        socket.close();
        
        System.out.println(outImg);
        // Write image from byte[] into file
//        Path outPath = Paths.get("received.jpg");
//        Files.write(
//                outPath,
//                imageToBytes(outImg),
//                StandardOpenOption.CREATE,
//                StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Hello to Java Sockets Client!");
        //interactive();
        imgExchange();
        System.out.println("Goodbye!");
    }
}
