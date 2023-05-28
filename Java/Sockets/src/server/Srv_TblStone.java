package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import common.Helpers;
import common.MsgTblStone;
import common.MsgType;

public class Srv_TblStone {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5025;
    
    private static boolean _shtdwnCmd = false;
    private static HashMap<String, Queue<MsgTblStone>> _msgQueues = new HashMap<String, Queue<MsgTblStone>>();
    
    // Region: processMessage* methods
    public static MsgTblStone processMessage(MsgTblStone inMessage) {
        MsgTblStone outMessage;
        
        switch(inMessage.getType()) {
        case Login:
            outMessage = processMessageLogin(inMessage.getName());
            break;
        case Logout:
            outMessage = processMessageLogout(inMessage.getName());
            break;
        case Send:
            outMessage = processMessageSend(inMessage.getTo(), inMessage);
            break;
        case Receive:
            outMessage = processMessageReceive(inMessage.getName());
            break;
        default:
            outMessage = MsgTblStone.newStatusMessage("[Err] Unsupported message!");
        }
        
        return outMessage;
    }
    
    public static MsgTblStone processMessageLogin(String name) {
        System.out.print(">");
        if (_msgQueues.containsKey(name)) {
            return MsgTblStone.newStatusMessage("[Err] Already logged in!");
        }
        Queue<MsgTblStone> msgQueue = new LinkedList<MsgTblStone>();
        _msgQueues.put(name, msgQueue);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    public static MsgTblStone processMessageLogout(String name) {
        System.out.print("<");
        if (!_msgQueues.containsKey(name)) {
            return MsgTblStone.newStatusMessage("[Err] Not logged in!");
        }
        _msgQueues.remove(name);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }

    public static MsgTblStone processMessageSend(String toName, MsgTblStone message) {
        System.out.print("+");

        if (toName.equals(".") && message.getData().equalsIgnoreCase("shtdwn")) {
            System.out.println("\nShutting down...");
            _shtdwnCmd = true;
            return MsgTblStone.newStatusMessage("[Success] Server shut down!");
        }

        if (!_msgQueues.containsKey(toName)) {
            return MsgTblStone.newStatusMessage("[Err] Unknown recipient!");
        }
        
        Queue<MsgTblStone> msgQueue = _msgQueues.get(toName);
        if (msgQueue == null) {
            msgQueue = new LinkedList<MsgTblStone>();
            _msgQueues.put(toName, msgQueue);
        }
        
        msgQueue.add(message);
        return MsgTblStone.newStatusMessage("[Success] OK!");
    }
    
    public static MsgTblStone processMessageReceive(String name) {
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
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InetAddress ipAddr = InetAddress.getByAddress(Helpers.ipToBytes(_IP));
        SocketAddress endPoint = new InetSocketAddress(ipAddr, _PORT);  
        ServerSocket server = new ServerSocket();
        server.bind(endPoint);
        System.out.println("Server ready!\n");
        
        do {
            // Wait for the socket connecting to a client
            Socket socket = server.accept();

            // Use the input stream of the socket to get the message from the client
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            MsgTblStone inMessage = (MsgTblStone)inStream.readObject();
            
            // Process the incoming message and get the response message in return
            MsgTblStone outMessage = processMessage(inMessage);
            
            // Use the output stream of the socket to respond to the client with a status message
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject(outMessage);
        } while(!_shtdwnCmd);
        
        server.close();
        System.out.println("Server is shutdown! Goodbye!");
    }
}
