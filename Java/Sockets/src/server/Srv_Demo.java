package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Srv_Demo {

    public static void main(String[] args)
            throws IOException, ClassNotFoundException {
        // print server address.
        InetAddress srvAddress = InetAddress.getLocalHost();
        System.out.printf("Server %s starting!\n", srvAddress.getHostAddress());
        
        // create a server socket, bound to a free IP port and start
        // waiting for a client to connect to this endpoint.
        ServerSocket server = new ServerSocket(5015);
        Socket socket = server.accept();
        
        // connection accepted, read and print the incoming message
        InputStream in = socket.getInputStream();
        ObjectInputStream objIn = new ObjectInputStream(in);
        String message = (String)objIn.readObject();
        System.out.printf("Server received: %s\n", message);
        
        // respond with an outgoing message
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        System.out.println("Server sending: Pong!\n");
        objOut.writeObject("Pong!");
        
        // close connection and server sockets.
        socket.close();
        server.close();
    }
}
