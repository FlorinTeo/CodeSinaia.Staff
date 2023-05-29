package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import common.MsgTblStone;

public class CliTblStone {
    
    private static String SERVER_IP = "192.168.4.132"; // TODO: Replace with the server's actual address!
    private static int SERVER_PORT = 5025;

    // Region: parseMessage* utility methods
    public static MsgTblStone parseMessage(String message) throws UnknownHostException {
        String[] parts = message.split(" ", 2);
        if (parts[0].equalsIgnoreCase("login")) {
            return parseMessageLogin(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("logout")) {
            return parseMessageLogout(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("send")) {
            return parseMessageSend(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("receive")) {
            return parseMessageReceive(parts[1].split(" "));
        } else {
            throw new RuntimeException("##Err##: Unrecognized command!");
        }
    }

    public static MsgTblStone parseMessageLogin(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        return MsgTblStone.newLoginMessage(name);
    }
    
    public static MsgTblStone parseMessageLogout(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        return MsgTblStone.newLogoutMessage(name);
    }
    
    public static MsgTblStone parseMessageSend(String[] args) throws UnknownHostException {
        if (args.length != 3
            || !args[0].startsWith("from:")
            || !args[1].startsWith("to:")
            || args[2].length() != 6) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String from = args[0].substring("from:".length());
        String to = args[1].substring("to:".length());
        char[] data = args[2].toCharArray();
        return MsgTblStone.newSendMessage(from, to, data);
    }
    
    public static MsgTblStone parseMessageReceive(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        return MsgTblStone.newReceiveMessage(name);
    }
    // EndRegion: parseMessage* utility methods
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Scanner console = new Scanner(System.in);
        
        do {
            System.out.print("Message?> ");
            String line = console.nextLine();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            
            try {
                // Prepare the outgoing message
                MsgTblStone message = parseMessage(line);
    
                // Create a socket connecting to the server
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                
                // Use the output stream of that socket to send the message to server
                ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.printf("--> %s\n", message.toString());
                objOutStream.writeObject(message);
                
                // Use the input stream of that socket to receive the response from server
                ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
                message = (MsgTblStone)objInStream.readObject();
                System.out.printf("<-- %s\n", message.toString());
                
                socket.close();
                System.out.println("DONE");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while(true);
        
        console.close();
        System.out.println("Client is done! Goodbye!");
    }
}
