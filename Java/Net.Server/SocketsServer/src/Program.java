import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import javax.imageio.ImageIO;

public class Program {
    
    private static final String _IP = "10.69.112.225"; //"10.69.112.155";
    private static final int _PORT = 5015;

    private static byte[] ipToBytes(String strIP) {
        byte[] byteIP = new byte[4];
        int i = 0;
        for(String strNum : strIP.split("\\.")) {
            byteIP[i++] = (byte)Integer.parseInt(strNum);
        }
        return byteIP;
    }
    
    private static void interactive() throws IOException {
        InetAddress ipAddr = InetAddress.getByAddress(ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        do {
            System.out.println("Waiting for client!");
            Socket socket = server.accept();
            
            System.out.print("Message received: ");
            InputStream rawStream = socket.getInputStream();
            BufferedInputStream bufferedStream = new BufferedInputStream(rawStream);
            DataInputStream dataStream = new DataInputStream(bufferedStream);
            
            String message = dataStream.readUTF();
            System.out.println(message);
            
            if (message.equalsIgnoreCase("shutdown")) {
                break;
            }
        } while(true);

        server.close();
    }
    
    private static BufferedImage toGrayScale(BufferedImage img) {
        return img;
    }
    
    private static void imgExchange() throws IOException {
        InetAddress ipAddr = InetAddress.getByAddress(ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        do {
            System.out.println("Waiting for client!");
            Socket socket = server.accept();
            
            // Receive image from the socket
            System.out.print("Image received: ");
            InputStream inStream = socket.getInputStream();
            BufferedImage inImg = ImageIO.read(inStream);
            
            // convert image to gray scale
            BufferedImage outImg = toGrayScale(inImg);
            
            // Send image to the socket
            ByteArrayOutputStream outImgStream = new ByteArrayOutputStream();
            ImageIO.write(outImg, "jpg", outImgStream);
            byte[] outImgBytes = outImgStream.toByteArray();
            OutputStream outStream = socket.getOutputStream();
            outStream.write(outImgBytes);
            outStream.close();
            break;
        } while(true);

        server.close();
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Hello to Java Sockets Server!");
        //interactive();
        imgExchange();
        System.out.println("Server shut down!");
    }

}
