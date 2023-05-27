package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import common.Helpers;
import common.MsgTblStone;
import common.MsgType;

public class Srv_TblStone {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5025;
    
    public static MsgTblStone processMessage(MsgTblStone inMessage) {
        switch(inMessage.getType()) {
        case Login:
            System.out.println(">>>> Client login");
            break;
        case Logout:
            System.out.println(">>>> Client logout");
            break;
        case Send:
            System.out.println(">>>> Client sending data");
            break;
        case Receive:
            System.out.println(">>>> Client requesting data");
            break;
        case Status:
            System.out.println(">>>> Client sending status?");
            break;
        default:
            System.out.println(">>>> Unsupported message!");
        }
        
        return MsgTblStone.newStatusMessage("OK!");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress ipAddr = InetAddress.getByAddress(Helpers.ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        do {
            System.out.println("Waiting for client!");
            
            // Wait for the socket connecting to a client
            Socket socket = server.accept();
            System.out.println("Message received: ");

            // Use the input stream of the socket to get the message from the client
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            MsgTblStone inMessage = (MsgTblStone)inStream.readObject();
            
            // Process the incoming message and get the response message in return
            MsgTblStone outMessage = processMessage(inMessage);
            
            // Use the output stream of the socket to respond to the client with a status message
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject(outMessage);
            
            // Shutting down on special command
            if (inMessage.getType() == MsgType.Send && inMessage.getData().equalsIgnoreCase("shtdwn")) {
                System.out.println("Shutting down...");
                break;
            }
            
        } while(true);
        
        server.close();
        System.out.println("Server is shutdown! Goodbye!");
    }
}
