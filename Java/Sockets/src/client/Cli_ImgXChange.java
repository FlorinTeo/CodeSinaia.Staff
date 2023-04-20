package client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import common.Helpers;

public class Cli_ImgXChange {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5015;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Hello to Java Sockets Client!");
        // Read image from file
        BufferedImage inImg = Helpers.readImage("../data/shelter.jpg");
        System.out.print("Image read .. ");
        // Open socket to server
        Socket socket = new Socket(_IP, _PORT);
        // Send image for processing
        Helpers.sendImage(inImg, socket);
        System.out.print("sent .. ");
        // Receive processed image
        BufferedImage outImg = Helpers.receiveImage(socket);
        System.out.print("received .. ");
        // Write processed image to file
        Helpers.writeImage(outImg, "../data/shelter_bw.jpg");
        System.out.println("written!");
        // Cleanup
        socket.close();
<<<<<<< HEAD:Java/Sockets/src/client/Program.java
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File f= new File(".");
        System.out.println(f.getAbsolutePath());
        System.out.println("Hello to Java Sockets Client!");
        imgExchange();
=======
>>>>>>> 45740dce6df1a6601b0513aebcc56691339541a0:Java/Sockets/src/client/Cli_ImgXChange.java
        System.out.println("Goodbye!");
    }
}
