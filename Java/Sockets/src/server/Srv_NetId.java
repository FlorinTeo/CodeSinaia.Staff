package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import common.Helpers;
import common.MsgNetId;
import common.MsgTblStone;

public class Srv_NetId {
    
    private static final String _IP = "127.0.0.1"; //"10.84.160.20";
    private static final int _PORT = 5025;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress ipAddr = InetAddress.getByAddress(Helpers.ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        
        do {
            System.out.println("Waiting for client registrations!");
            Socket socket = server.accept();
            
            System.out.println("Message received: ");
            ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
            MsgNetId message = (MsgNetId)objInStream.readObject();
            System.out.println(message);
        } while(true);
    }
}
