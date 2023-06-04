package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import common.MsgTblStone;

/**
 * Class implementing the messenger functionality in the "Tablets of Stone" game: It is a socket-based server
 * able to answer to a simple set of commands: Login/Logout/Send/Receive. Clients are expected to login before
 * being able to either send or receive messages.
 */
public class SrvTblStone {
    
    // The port bound by the server for listening
    private static final int _PORT = 5025;
    // Server listens and accepts messages from clients for as long as the shutdown command below is false.
    private static boolean _shtdwnCmd = false;
    // Hash map associating the name of a client with a queue of messages sent to that client.
    private static Map<String, Queue<MsgTblStone>> _msgQueues = new HashMap<String, Queue<MsgTblStone>>();
    // Hash map associating the name of a client with its internet address as detected during login.
    private static Map<InetAddress, String> _inetMap = new HashMap<InetAddress, String>();
    // Probability of a message to be dropped
    private static double _chanceDrop = 0.0;
    // Probability of a message to be reordered
    private static double _chanceReorder = 0.0;
    
    // Region: processMessage* methods
    /**
     * Processes a given input c returning an output (response) message.
     * @param inetAddress - Internet address of the sender of this message.
     * @param inMessage - message to be processed.
     * @return response message.
     * @throws UnknownHostException
     * @see SrvTblStone#processMessageLogin(InetAddress, String)
     * @see SrvTblStone#processMessageLogout(InetAddress, String)
     * @see SrvTblStone#processMessageSend(InetAddress, String, String, MsgTblStone)
     * @see SrvTblStone#processMessageReceive(InetAddress, String)
     */
    public static MsgTblStone processMessage(InetAddress inetAddress, MsgTblStone inMessage) throws UnknownHostException {
        MsgTblStone outMessage;

        switch(inMessage.getType()) {
        case Login:
            outMessage = processMessageLogin(inetAddress, inMessage.getName());
            break;
        case Logout:
            outMessage = processMessageLogout(inetAddress, inMessage.getName());
            break;
        case Send:
            outMessage = processMessageSend(inetAddress, inMessage.getFrom(), inMessage.getTo(), inMessage);
            break;
        case Receive:
            outMessage = processMessageReceive(inetAddress, inMessage.getName());
            break;
        case Status:
            outMessage = processMessageStatus(inetAddress, inMessage.getStatus());
            break;
        default:
            outMessage = MsgTblStone.newStatusMessage("[Err] Unsupported message!");
        }
        
        return outMessage;
    }
    
    /**
     * Processes a Login message for the given client name. If the client is already logged
     * in the operation fails and a failing Status message is returned to the caller.
     * @param inetAddress - Internet address of the sender of this message.
     * @param name - The name of the client logging in.
     * @return Status message indicating success or failure.
     * @throws UnknownHostException
     * @see SrvTblStone#processMessageLogout(InetAddress, String)
     * @see SrvTblStone#processMessage(InetAddress, MsgTblStone)
     */
    public static MsgTblStone processMessageLogin(InetAddress inetAddress, String name) throws UnknownHostException {
        if (_inetMap.containsKey(inetAddress)) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Already logged in!");
        }
        System.out.print("+");
        _inetMap.put(inetAddress, name);
        _msgQueues.put(name, new LinkedList<MsgTblStone>());
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    /**
     * Processes a Logout message for the given client name. If the client is not already
     * logged in the operation fails and a failing Status message is returned to the caller.
     * @param inetAddress - Internet address of the sender of this message.
     * @param name - The name of the client logging out.
     * @return Status message indicating success or failure.
     * @throws UnknownHostException
     * @see SrvTblStone#processMessageLogin(InetAddress, String)
     * @see SrvTblStone#processMessage(InetAddress, MsgTblStone)
     */
    public static MsgTblStone processMessageLogout(InetAddress inetAddress, String name) throws UnknownHostException {
        String registeredName = _inetMap.get(inetAddress);
        if (registeredName == null || !registeredName.equals(name)) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Denied: Spoofing!");
        }
        System.out.print("-");
        _inetMap.remove(inetAddress);
        _msgQueues.remove(name);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }

    /**
     * Processes a Send message for a given sender, targetting the given recepient and
     * containing the given message as payload. If either the send or the recepient
     * are not logged in, the operation fails and a failing Status message is returned to the caller.
     * @param inetAddress - Internet address of the sender of this message.
     * @param from - the name of the client sending the message.
     * @param to - the name of the client to receive the message.
     * @param message - the message to be relayed.
     * @return Status message indicating success or failure.
     * @throws UnknownHostException
     * @see SrvTblStone#processMessageReceive(InetAddress, String)
     * @see SrvTblStone#processMessage(InetAddress, MsgTblStone)
     */
    public static MsgTblStone processMessageSend(InetAddress inetAddress, String from, String to, MsgTblStone message) throws UnknownHostException {
        if (!_inetMap.containsKey(inetAddress)) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Denied: Repudation!");
        }

        if (!_msgQueues.containsKey(from)) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Unknown sender!");
        }

        if (to.equals(".") && message.getData().equalsIgnoreCase("shtdwn")) {
            System.out.println("\nShutting down...");
            _shtdwnCmd = true;
            return MsgTblStone.newStatusMessage("[Success] Server shut down!");
        }

        if (!_msgQueues.containsKey(to)) 
        {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Unknown recipient!");
        }
        
        if (Math.random() < _chanceDrop) {
            System.out.print("v");
        } else {
            Queue<MsgTblStone> msgQueue = _msgQueues.get(to);
            if (msgQueue == null) {
                msgQueue = new LinkedList<MsgTblStone>();
                _msgQueues.put(to, msgQueue);
            }
            if (Math.random() < _chanceReorder) {
                System.out.print("~");
                int iMessage = (int)(Math.random() * msgQueue.size());
                ((LinkedList<MsgTblStone>)msgQueue).add(iMessage, message);
            } else {
                System.out.print(">");
                msgQueue.add(message);
            }
        }
        
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    /**
     * Processes a Receive message for a given client. The method takes as parameter the client name
     * expecting to receive the message. If such a message is available, the method returns it as
     * a Send message, encapsulating the Sender's name, IP address and data playload. If no such
     * message exists, the method returns a failing Status message.
     * @param inetAddress - Internet address of the sender of this message.
     * @param to - the name of the client requesting a message.
     * @return either a Send message or a Status indicating success or failure.
     * @throws UnknownHostException
     * @see SrvTblStone#processMessageSend(InetAddress, String, String, MsgTblStone)
     * @see SrvTblStone#processMessage(InetAddress, MsgTblStone)
     */
    public static MsgTblStone processMessageReceive(InetAddress inetAddress, String name) throws UnknownHostException {
        String registeredName = _inetMap.get(inetAddress);
        if (registeredName == null || !registeredName.equals(name)) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Denied: Spoofing!");
        }
        
        Queue<MsgTblStone> msgQueue = _msgQueues.get(name);
        if (msgQueue == null || msgQueue.size() == 0) {
            System.out.print("?");
            return MsgTblStone.newStatusMessage("[Err] No message!");
        }
        
        System.out.print("<");    
        return msgQueue.remove();
    }
    
    /**
     * Processes a Status message from a registered client. The message contains an "operation" request
     * in its status string, which can be either an "inquire" for server internal status or a 
     * "reset" directive to forcefully logout all clients and purge all message queues.
     * @param inetAddress - Internet address of the sender of this message.
     * @param status - the status string
     * @return a Status message indicating success or failure.
     * @throws UnknownHostException
     */
    private static MsgTblStone processMessageStatus(InetAddress inetAddress, String status) throws UnknownHostException {
        String registeredName = _inetMap.get(inetAddress);
        if (registeredName == null) {
            System.out.print("x");
            return MsgTblStone.newStatusMessage("[Err] Denied: Info Disclosure!");
        }
        
        String info = "[Success]";
        switch(status.toLowerCase()) {
        case "inquire":
            for(Map.Entry<InetAddress, String> kvp: _inetMap.entrySet()) {
                info += "\n" + kvp.getKey().toString() + " : " + kvp.getValue();
            }
            System.out.print("i");
            break;
        case "reset":
            _inetMap.clear();
            _msgQueues.clear();
            System.out.print("r");
            break;
        default:
            info = "[Err] Unsupported operation!";
        }
        
        return MsgTblStone.newStatusMessage(info);
    }
    // EndRegion: processMessage* methods
    
    /**
     * Main entry point in the program. The server loops continuously, waiting for a message from any client.
     * When message is received, it is deserialized, processed, and a response message is sent back.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress inetAddr = InetAddress.getLocalHost();
        ServerSocket server = new ServerSocket(_PORT);
        System.out.printf("[%s] Server ready!\n", inetAddr.getHostAddress());
        
        do {
            Socket socket = null;
            
            try {
                // Wait for the socket connecting to a client
                socket = server.accept();
    
                // Use the input stream of the socket to get the message from the client
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                MsgTblStone inMessage = (MsgTblStone)inStream.readObject();
                
                // Process the incoming message and get the response message in return
                MsgTblStone outMessage = processMessage(socket.getInetAddress(), inMessage);
                
                // Use the output stream of the socket to respond to the client with a status message
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.writeObject(outMessage);
            } catch (Exception e) {
                System.out.print("X");
            }
            
            if (socket != null) {
                socket.close();
            }
        } while(!_shtdwnCmd);
        
        server.close();
        System.out.println("Server is shutdown! Goodbye!");
    }
}
