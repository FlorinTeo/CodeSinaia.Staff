import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

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
    
    public static void main(String[] args) throws IOException {
        System.out.println("Hello to Java Sockets Server!");
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
        System.out.println("Server shut down!");

    }

}
