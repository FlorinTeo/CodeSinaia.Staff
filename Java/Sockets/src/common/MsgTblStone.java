package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;

    // Command code for this message
    private MsgType _msgType;
    
    // Fields for MsgCode.Register and MsgCode.Receive
    private String _name;
    
    // Fields for MsgCode.Send
    private String _from;
    private String _to;
    private char[] _data;
        
    // Response status for all MsgCode.*
    private String _status;
    
    // Region: Factory methods
    /**
     * Creates a new generic message, filling in only the necessary fields,
     * according to the message type.
     */
    private static MsgTblStone newMessage(
            MsgType msgType,
            Object... args) {
        MsgTblStone message = new MsgTblStone();
        message._msgType = msgType;
        
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
    
    public static MsgTblStone newLoginMessage(String name) {
        return newMessage(MsgType.Login, name);
    }
    
    public static MsgTblStone newLogoutMessage(String name) {
        return newMessage(MsgType.Logout, name);
    }
    
    public static MsgTblStone newSendMessage(String from, String to, char[] data) {
        return newMessage(MsgType.Send, from, to, data);
    }
    
    public static MsgTblStone newReceiveMessage(String name) {
        return newMessage(MsgType.Receive, name);
    }
    
    public static MsgTblStone newStatusMessage(String status) {
        return newMessage(MsgType.Status, status);
    }
    // EndRegion: FactoryMethods
    
    // Region: Accessors
    public MsgType getType() {
        return _msgType;
    }
    
    public String getFrom() {
        return _from;
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
              String.format("--[MsgType:%d]----\n", _msgType.ordinal())
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
