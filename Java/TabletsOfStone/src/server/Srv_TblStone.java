package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import common.MsgTblStone;

/**
 * This class iplements the messenger functionality in the "Tablets of Stone" game: It is a socket-based server
 * able to answer to a simple set of commands: login/logout/send/receive. Clients are expected to login before
 * being able to either send or receive a message.
 */
public class Srv_TblStone {
    
    // The port bound by the server for listening
    private static final int _PORT = 5025;
    // Server will listen and accept messages from clients for as long as the shut down command below is false.
    private static boolean _shtdwnCmd = false;
    // Hash map associating the name of a client with a queue of messages sent to that client.
    private static HashMap<String, Queue<MsgTblStone>> _msgQueues = new HashMap<String, Queue<MsgTblStone>>();
    
    // Region: processMessage* methods
    public static MsgTblStone processMessage(String ipAddress, MsgTblStone inMessage) throws UnknownHostException {
        MsgTblStone outMessage;
        
        if (!ipAddress.equalsIgnoreCase(inMessage.getIp())) {
            return MsgTblStone.newStatusMessage("[Err] Tampered message!");
        }
        
        switch(inMessage.getType()) {
        case Login:
            outMessage = processMessageLogin(inMessage.getName());
            break;
        case Logout:
            outMessage = processMessageLogout(inMessage.getName());
            break;
        case Send:
            outMessage = processMessageSend(inMessage.getFrom(), inMessage.getTo(), inMessage);
            break;
        case Receive:
            outMessage = processMessageReceive(inMessage.getName());
            break;
        default:
            outMessage = MsgTblStone.newStatusMessage("[Err] Unsupported message!");
        }
        
        return outMessage;
    }
    
    public static MsgTblStone processMessageLogin(String name) throws UnknownHostException {
        System.out.print(">");
        if (_msgQueues.containsKey(name)) {
            return MsgTblStone.newStatusMessage("[Err] Already logged in!");
        }
        Queue<MsgTblStone> msgQueue = new LinkedList<MsgTblStone>();
        _msgQueues.put(name, msgQueue);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    public static MsgTblStone processMessageLogout(String name) throws UnknownHostException {
        System.out.print("<");
        if (!_msgQueues.containsKey(name)) {
            return MsgTblStone.newStatusMessage("[Err] Not logged in!");
        }
        _msgQueues.remove(name);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }

    public static MsgTblStone processMessageSend(String from, String to, MsgTblStone message) throws UnknownHostException {
        System.out.print("+");

        if (!_msgQueues.containsKey(from)) {
            return MsgTblStone.newStatusMessage("[Err] Unknown sender!");
        }

        if (to.equals(".") && message.getData().equalsIgnoreCase("shtdwn")) {
            System.out.println("\nShutting down...");
            _shtdwnCmd = true;
            return MsgTblStone.newStatusMessage("[Success] Server shut down!");
        }

        if (!_msgQueues.containsKey(to)) {
            return MsgTblStone.newStatusMessage("[Err] Unknown recipient!");
        }
        
        Queue<MsgTblStone> msgQueue = _msgQueues.get(to);
        if (msgQueue == null) {
            msgQueue = new LinkedList<MsgTblStone>();
            _msgQueues.put(to, msgQueue);
        }
        
        msgQueue.add(message);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    public static MsgTblStone processMessageReceive(String name) throws UnknownHostException {
        System.out.print("?");
        
        if (!_msgQueues.containsKey(name)) {
            return MsgTblStone.newStatusMessage("[Err] Not logged in!");
        }
        
        Queue<MsgTblStone> msgQueue = _msgQueues.get(name);
        if (msgQueue == null || msgQueue.size() == 0) {
            return MsgTblStone.newStatusMessage("[Err] No message!");
        }
        
        return msgQueue.remove();
    }
    // EndRegion: processMessage* methods
    
    /**
     * Main entry point in the program. The server loops continuously, waiting for a message from any client.
     * When message is received, it is deserialized, processed, and a response message is sent back.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress inetAddr = InetAddress.getLocalHost();
        SocketAddress endPoint = new InetSocketAddress(inetAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        System.out.printf("[%s] Server ready!\n", inetAddr.getHostAddress());
        
        do {
            // Wait for the socket connecting to a client
            Socket socket = server.accept();
            String clientIpAddress = socket.getInetAddress().getHostAddress();

            // Use the input stream of the socket to get the message from the client
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            MsgTblStone inMessage = (MsgTblStone)inStream.readObject();
            
            // Process the incoming message and get the response message in return
            MsgTblStone outMessage = processMessage(clientIpAddress, inMessage);
            
            // Use the output stream of the socket to respond to the client with a status message
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject(outMessage);
        } while(!_shtdwnCmd);
        
        server.close();
        System.out.println("Server is shutdown! Goodbye!");
    }
}
