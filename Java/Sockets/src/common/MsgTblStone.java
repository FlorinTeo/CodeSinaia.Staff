package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;

    // Command code for this message
    private MsgType _msgType;
    
    // IP address of the creator of this message
    private String _ipAddress;
    
    // Fields for MsgCode.Login,Logout,Receive
    private String _name;
    
    // Fields for MsgCode.Send
    private String _from;
    private String _to;
    private char[] _data;
        
    // Response status for all MsgCode.*
    private String _status;
    
    // Region: Factory methods
    /**
     * Creates a new generic message, filling in only the necessary fields, according to the message type.
     */
    private static MsgTblStone newMessage(MsgType msgType, Object... args) throws UnknownHostException {
        MsgTblStone message = new MsgTblStone();
        message._msgType = msgType;
        message._ipAddress = InetAddress.getLocalHost().getHostAddress();
        
        switch(msgType) {
        case Login:
        case Logout:
            message._name = (String)args[0];
            break;
        case Send:
            message._from = (String)args[0];
            message._to = (String)args[1];
            char[] data = (char[]) args[2];
            if (data.length != 6) {
                throw new RuntimeException("Invalid data size!");
            }
            message._data = data;
            break;
        case Receive:
            message._name = (String)args[0];
            break;
        case Status:
            message._status = (String)args[0];
            break;
        }        
        return message;
    }
    
    public static MsgTblStone newLoginMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Login, name);
    }
    
    public static MsgTblStone newLogoutMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Logout, name);
    }
    
    public static MsgTblStone newSendMessage(String from, String to, char[] data) throws UnknownHostException {
        return newMessage(MsgType.Send, from, to, data);
    }
    
    public static MsgTblStone newReceiveMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Receive, name);
    }
    
    public static MsgTblStone newStatusMessage(String status) throws UnknownHostException {
        return newMessage(MsgType.Status, status);
    }
    // EndRegion: FactoryMethods
    
    // Region: Accessors
    public MsgType getType() {
        return _msgType;
    }
    
    public String getIp() {
        return _ipAddress;
    }
    
    public String getName() {
        return _name;
    }
    
    public String getFrom() {
        return _from;
    }
    
    public String getTo() {
        return _to;
    }
    
    public String getData() {
        return new String(_data);
    }
    
    public String getStatus() {
        return _status;
    }
    // EndRegion: Accessors
    
    // Region: Serialization / Deserialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(_msgType);
        out.writeObject(_ipAddress);
        switch(_msgType) {
        case Login:
        case Logout:
            out.writeObject(_name);
            break;
        case Send:
            out.writeObject(_from);
            out.writeObject(_to);
            out.writeObject(_data);
            break;
        case Receive:
            out.writeObject(_name);
            break;
        case Status:
            out.writeObject(_status);
            break;
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        _msgType = (MsgType)in.readObject();
        _ipAddress = (String)in.readObject();
        switch(_msgType) {
        case Login:
        case Logout:
            _name = (String)in.readObject();
            break;
        case Send:
            _from = (String)in.readObject();
            _to = (String)in.readObject();
            _data = (char[])in.readObject();
            break;
        case Receive:
            _name = (String)in.readObject();
            break;
        case Status:
            _status = (String)in.readObject();
            break;
        }
    }
    // EndRegion:Serialization/Deserialization
    
    @Override
    public String toString() {
        String output = 
              String.format("--[IP:%s:MsgType:%d]----\n", _ipAddress, _msgType.ordinal())
            + toString(_msgType);
        return output;
    }
    
    public String toString(MsgType msgType) {
        String output = "";
        switch(msgType) {
        case Login:
        case Logout:
            output = 
                  String.format("  [Name: %s]\n", _name);
            break;
        case Send:
            output = 
                  String.format("  [From: %s]\n", _from)
                + String.format("  [To: %s]\n" , _to)
                + String.format("  \"%s\"\n", new String(_data));
            break;
        case Receive:
            output = 
                  String.format("  [Name: %s]\n", _name);
            break;
        case Status:
            output = "  " + _status;
            break;
        default:
            output = "  Undefined!";
        }
        return output;
    }
}
