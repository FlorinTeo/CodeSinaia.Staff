package server;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import common.Helpers;

public class Srv_ImgXChange {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5015;
    
    private static BufferedImage toGrayScale(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color col = new Color(image.getRGB(x, y));
                int r = col.getRed();
                int g = col.getGreen();
                int b = col.getBlue();
                int gray = (r + g + b) / 3;
                col = new Color(gray, gray, gray);
                image.setRGB(x, y, col.getRGB());
            }
        }
        return image;
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Hello to Java Sockets Server!");
        InetAddress ipAddr = InetAddress.getByAddress(Helpers.ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        System.out.println("Waiting for client!");
        Socket socket = server.accept();
        
        // Receive image from the socket
        BufferedImage inImage = Helpers.receiveImage(socket);
        System.out.print("Image received .. ");
        // Process image to gray scale
        BufferedImage outImage = toGrayScale(inImage);
        System.out.print("processed .. ");
        // Send converted image back to the client
        Helpers.sendImage(outImage, socket);
        System.out.println("returned!");
        // Cleanup socket
        socket.close();
        // Shutdown server
        server.close();
        System.out.println("Server shut down!");
    }
}
