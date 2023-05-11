package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;

public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;

    // Command code for this message
    private MsgType _msgType;
    
    // Fields for MsgCode.Register and MsgCode.Receive
    private String _name;
    private InetAddress _inetAddress;
    
    // Fields for MsgCode.Send
    private String _from;
    private String _to;
    private char[] _data;
        
    // Response status for all MsgCode.*
    private String _status;
    
    /**
     * Constructs a (MsgCode.Identity) message fit for registering a client with the server.
     * @param msgCode
     * @param name
     */
    public MsgTblStone(String name, InetAddress inetAddress) {
        _msgType = MsgType.Identity;
        _name = name;
        _inetAddress = inetAddress;
    }
    
    /**
     * Constructs a (MsgType.Data) message fit for sending content from one client to another.
     * @param from
     * @param to
     * @param data
     */
    public MsgTblStone(String from, String to, char[] data) {
        _msgType = MsgType.Data;
        if (data.length != 6) {
            throw new RuntimeException("Invalid data size!");
        }
        _data = data;
        _from = from;
        _to = to;
    }
    
    /**
     * Constructs a (MsgType.DataRequest) message fit for requesting the
     * next message available for the given client.
     * @param name
     */
    public MsgTblStone(String name) {
        _msgType = MsgType.DataRequest;
        _name = name;
    }
    
    /**
     * Constructs a (MsgType.Status) message fit for an "OK" network exchange status
     * @return
     */
    public MsgTblStone() {
        _msgType = MsgType.Status;
        _status = "OK";
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
    
    public void setStatus(String status) {
        _status = status;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(_msgType);        
        switch(_msgType) {
        case Identity:
            out.writeObject(_name);
            out.writeObject(_inetAddress);
            break;
        case DataRequest:
            out.writeObject(_name);
            break;
        case Data:
            out.writeObject(_from);
            out.writeObject(_to);
            out.writeObject(_data);
            break;
        case Status:
            out.writeObject(_status);
            break;
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        _msgType = (MsgType)in.readObject();
        switch(_msgType) {
        case Identity:
            _name = (String)in.readObject();
            _inetAddress = (InetAddress)in.readObject();
            break;
        case DataRequest:
            _name = (String)in.readObject();
            break;
        case Data:
            _from = (String)in.readObject();
            _to = (String)in.readObject();
            _data = (char[])in.readObject();
            break;
        case Status:
            _status = (String)in.readObject();
            break;
        }
    }
    
    @Override
    public String toString() {
        String output = 
              String.format("--[%d]----\n", _msgType)
            + toString(_msgType);
        return output;
    }
    
    public String toString(MsgType msgType) {
        String output = "";
        switch(msgType) {
        case Identity:
            break;
        case DataRequest:
            break;
        case Data:
            output = 
                  String.format("  [From: %s]\n", _from)
                + String.format("  [To: %s]\n" , _to)
                + String.format("  \"%s\"\n", new String(_data));
            break;
        case Status:
            output = "    Undefined!";
            break;
        }
        return output;
    }
}
