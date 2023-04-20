package client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import common.Helpers;

public class Program {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5015;
    
    private static void imgExchange() throws IOException, ClassNotFoundException {
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
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File f= new File(".");
        System.out.println(f.getAbsolutePath());
        System.out.println("Hello to Java Sockets Client!");
        imgExchange();
        System.out.println("Goodbye!");
    }
}
