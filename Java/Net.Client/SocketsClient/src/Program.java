import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Program {
    
    private static final String _IP = "10.69.112.225"; //"10.69.112.155";
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

    private static void imgExchange() throws IOException {
        // Read image from file in byte[]
        String inFile = "shelter.jpg";
        BufferedImage inImg = ImageIO.read(new File(inFile));
        ByteArrayOutputStream inImgStream = new ByteArrayOutputStream();
        ImageIO.write(inImg, "jpg", inImgStream);
        byte[] inImgBytes = inImgStream.toByteArray();
        
        // Write byte[] to server
        Socket socket = new Socket(_IP, _PORT);
        OutputStream outStream = socket.getOutputStream();
        outStream.write(inImgBytes);
        outStream.close();
        
        // Read answer from server in byte[]
        InputStream inStream = socket.getInputStream();
        BufferedImage outImg = ImageIO.read(inStream);
        ByteArrayOutputStream outImgStream = new ByteArrayOutputStream();
        ImageIO.write(outImg, "jpg", outImgStream);
        byte[] outImgBytes = outImgStream.toByteArray();
        
        // Write image from byte[] into file
        Path outPath = Paths.get("received.jpg");
        Files.write(outPath, outImgBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        socket.close();
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Hello to Java Sockets Client!");
        //interactive();
        imgExchange();
        System.out.println("Goodbye!");
    }
}