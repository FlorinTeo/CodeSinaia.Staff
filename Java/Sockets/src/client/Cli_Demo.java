package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cli_Demo {

    public static void main(String[] args) 
            throws UnknownHostException, IOException, ClassNotFoundException {
        // create a socket connected to the server endpoint (address + port)
        System.out.println("Client Starting!");
        Socket socket = new Socket("192.168.4.132", 5015);
        
        // write a message to that connection
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        System.out.println("Client sending: Ping!");
        objOut.writeObject("Ping!");
        
        // wait for an answer, read it and print it to the console
        InputStream in = socket.getInputStream();
        ObjectInputStream objIn = new ObjectInputStream(in);
        String response = (String)objIn.readObject();
        System.out.printf("Client received: %s\n", response);
        
        // close the connection socket.
        socket.close();
    }
}


