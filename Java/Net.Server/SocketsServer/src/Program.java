import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import javax.imageio.ImageIO;

public class Program {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
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

    private static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baStream);
        return baStream.toByteArray();
    }
    
    private static BufferedImage bytesToImage(byte[] rawBytes) throws IOException {
        InputStream imageStream = new ByteArrayInputStream(rawBytes);
        return ImageIO.read(imageStream);
    }
    
    private static BufferedImage toGrayScale(BufferedImage img) {
        return img;
    }
    
    private static void imgExchange() throws IOException, ClassNotFoundException {
        InetAddress ipAddr = InetAddress.getByAddress(ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        System.out.println("Waiting for client!");
        Socket socket = server.accept();
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            
        // Receive image from the socket
        System.out.print("Image received: ");
        BufferedImage inImg = (BufferedImage)inStream.readObject();
            
        // convert image to gray scale
        BufferedImage outImg = toGrayScale(inImg);
            
        // Send image to the socket
        outStream.writeObject(outImg);
        
        // Close streams, socket and server
        outStream.close();
        inStream.close();
        socket.close();        
        server.close();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Hello to Java Sockets Server!");
        //interactive();
        imgExchange();
        System.out.println("Server shut down!");
    }

}
