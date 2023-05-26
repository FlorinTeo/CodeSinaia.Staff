package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import common.MsgTblStone;

public class Cli_TblStone {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5025;
    
    /**
     * Utility method parsing a message from 
     * @param message
     * @return
     */
    public static MsgTblStone parseMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length != 3
            || !parts[0].startsWith("from:")
            || !parts[1].startsWith("to:")
            || parts[2].length() != 6) {
            throw new RuntimeException("Invalid message!");
        }
        String from = parts[0].substring("from:".length());
        String to = parts[1].substring("to:".length());
        char[] data = parts[2].toCharArray();
        return MsgTblStone.newSendMessage(from, to, data);
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        
        Scanner console = new Scanner(System.in);
        
        do {
            System.out.print("Message?> ");
            String line = console.nextLine();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            
            // Prepare the outgoing message
            MsgTblStone message = parseMessage(line);

            // Create a socket connecting to the server
            Socket socket = new Socket(_IP, _PORT);
            
            // Use the output stream of that socket to send the message to server
            ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(message);
            objOutStream.writeObject(message);
            
            // Use the input stream of that socket to receive the response from server
            ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
            message = (MsgTblStone)objInStream.readObject();
            System.out.println(message);
            
            socket.close();
            System.out.println("DONE");

        } while(true);
        
        console.close();
        System.out.println("Client is done! Goodbye!");
    }

}
