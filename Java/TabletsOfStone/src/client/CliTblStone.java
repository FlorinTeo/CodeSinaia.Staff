package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import common.MsgTblStone;

public class CliTblStone {
    
    private static String SERVER_IP = "20.3.85.95"; // TODO: Replace with the server's actual address!
    private static int SERVER_PORT = 5025;

    // Region: parseMessage* utility methods
    public static MsgTblStone[] parseMessages(String message) throws UnknownHostException {
        String[] parts = message.split(" ", 2);
        if (parts[0].equalsIgnoreCase("login")) {
            return parseMessageLogin(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("logout")) {
            return parseMessageLogout(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("send")) {
            return parseMessagesSend(parts[1].split(" ", 3));
        } else if (parts[0].equalsIgnoreCase("receive")) {
            return parseMessagesReceive(parts[1].split(" "));
        } else if (parts[0].equalsIgnoreCase("status")) {
            return parseMessageStatus(parts[1].split(" "));
        } else {
            throw new RuntimeException("##Err##: Unrecognized command!");
        }
    }

    public static MsgTblStone[] parseMessageLogin(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        return new MsgTblStone[] { MsgTblStone.newLoginMessage(name) };
    }
    
    public static MsgTblStone[] parseMessageLogout(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        return new MsgTblStone[] { MsgTblStone.newLogoutMessage(name) };
    }
    
    public static MsgTblStone[] parseMessagesSend(String[] args) throws UnknownHostException {
        if (args.length != 3
            || !args[0].startsWith("from:")
            || !args[1].startsWith("to:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        
        String from = args[0].substring("from:".length());
        String to = args[1].substring("to:".length()).replace("_", " ");
        String text = args[2];

        List<MsgTblStone> messages = new LinkedList<MsgTblStone>();
        while(text.length() > 0) {
            MsgTblStone message;
            if (text.length() >= 6) {
                message = MsgTblStone.newSendMessage(from, to, text.substring(0, 6).toCharArray());
                text = text.substring(6);
            } else {
                message = MsgTblStone.newSendMessage(from, to, String.format("%-6s", text).toCharArray());
                text = "";
            }
            messages.add(message);
        }
        return messages.toArray(new MsgTblStone[messages.size()]);
    }

    public static MsgTblStone[] parseMessagesReceive(String[] args) throws UnknownHostException {
        if (args.length < 1 || args.length > 2 || !args[0].startsWith("name:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String name = args[0].substring("name:".length());
        Integer nMessages = 1;
        if (args.length == 2) {
            nMessages = Integer.parseInt(args[1]);
        }

        MsgTblStone[] messages = new MsgTblStone[nMessages];
        for(int i = 0; i < nMessages; i++) {
            messages[i] = MsgTblStone.newReceiveMessage(name);
        }
        
        return messages;
    }
    
    public static MsgTblStone[] parseMessageStatus(String[] args) throws UnknownHostException {
        if (args.length != 1 || !args[0].startsWith("op:")) {
            throw new RuntimeException("##Err##: Invalid syntax!");
        }
        String op = args[0].substring("op:".length());
        return new MsgTblStone[] { MsgTblStone.newStatusMessage(op) };
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
            
            Socket socket = null;
            try {
                // Prepare the outgoing message
                MsgTblStone[] messages = parseMessages(line);
    
                for (int i = 0; i < messages.length; i++) {
                    MsgTblStone message = messages[i]; 
                    // Create a socket connecting to the server
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    
                    // Use the output stream of that socket to send the message to server
                    ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
                    System.out.printf("[%d]--> %s\n", i, message.toString());
                    objOutStream.writeObject(message);
                    
                    // Use the input stream of that socket to receive the response from server
                    ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
                    message = (MsgTblStone)objInStream.readObject();
                    System.out.printf("<--[%d] %s\n", i, message.toString());
                }
                
                System.out.println("DONE");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            if (socket != null) {
                socket.close();
            }
        } while(true);
        
        console.close();
        System.out.println("Client is done! Goodbye!");
    }
}
