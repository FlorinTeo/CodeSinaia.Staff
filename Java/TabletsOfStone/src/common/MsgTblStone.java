package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;

/**
 * Class encapsulating a generic message exchanged between clients and server in the
 * "Tablets of Stone" game. The clients (governers in the game's story) can exchange
 * messages with other clients by relaying them through the server (messenger in the game's story)
 */
public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;

    // Message type code for this message
    private MsgType _msgType;
    
    // Fields for MsgCode.Login,Logout,Receive
    private String _name;
    
    // Fields for MsgCode.Send
    private String _from; // name of the sender
    private String _to;   // name of the recepient
    private char[] _data; // data payload
        
    // Fields for MsgCode.Status
    private String _status;
    
    // Region: Factory methods
    /**
     * Creates a new MsgTblStone object, of a given type, filling in all fields specifc to that type.
     * @param msgType - the type of the message being created.
     * @param args - variable list of arguments.
     * @return the new message of the given type with the content specific to its type.
     */
    private static MsgTblStone newMessage(MsgType msgType, Object... args) throws UnknownHostException {
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
    
    /**
     * Creates a new Login message.
     * @param name - the name of the client logging in.
     * @return the new Login message.
     * @throws UnknownHostException
     * @see {@link MsgTblStone#newMessage(MsgType, Object...)}
     */
    public static MsgTblStone newLoginMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Login, name);
    }
    
    /**
     * Creates a new Logout message.
     * @param name - the name of the client logging out.
     * @return the new Logout message.
     * @throws UnknownHostException
     * @see {@link MsgTblStone#newMessage(MsgType, Object...)}
     */
    public static MsgTblStone newLogoutMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Logout, name);
    }
    
    /**
     * Creates a new Send message.
     * @param from - the name of the client sending the message.
     * @param to - the name of the client receiving the message.
     * @param data - the data payload of the message.
     * @return the new Send message.
     * @throws UnknownHostException
     * @see {@link MsgTblStone#newMessage(MsgType, Object...)}
     */
    public static MsgTblStone newSendMessage(String from, String to, char[] data) throws UnknownHostException {
        return newMessage(MsgType.Send, from, to, data);
    }
    
    /**
     * Creates a new Receive message.
     * @param name - the name of the client requesting to receive the data.
     * @return the new Receive message.
     * @throws UnknownHostException
     * @see {@link MsgTblStone#newMessage(MsgType, Object...)}
     */
    public static MsgTblStone newReceiveMessage(String name) throws UnknownHostException {
        return newMessage(MsgType.Receive, name);
    }
    
    /**
     * Creates a new Status message.
     * @param status - the free-form string content for the status.
     * @return the new Status message.
     * @throws UnknownHostException
     * @see {@link MsgTblStone#newMessage(MsgType, Object...)}
     */
    public static MsgTblStone newStatusMessage(String status) throws UnknownHostException {
        return newMessage(MsgType.Status, status);
    }
    // EndRegion: FactoryMethods
    
    // Region: Accessors
    /**
     * Gives the type of this message (used for all message types).
     * @return the MsgType code of this message.
     */
    public MsgType getType() {
        return _msgType;
    }
    
    /**
     * Gives the client name (used for MsgType.Login,Logout,Receive).
     * @return the client name.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Gives the sender's name (used for MsgType.Send)
     * @return the sender's name.
     */
    public String getFrom() {
        return _from;
    }
    
    /**
     * Gives the recepient's name (used for MsgType.Receive)
     * @return the recepient's name.
     */
    public String getTo() {
        return _to;
    }
    
    /**
     * Gives the data paylod in this message (used for MsgType.Send).
     * @return the data payload.
     */
    public String getData() {
        return new String(_data);
    }
    
    /**
     * Gives the status string in this message (used for MsgType.Status).
     * @return the status string.
     */
    public String getStatus() {
        return _status;
    }
    // EndRegion: Accessors
    
    // Region: Serialization / Deserialization
    /**
     * Serializes this object to the given output stream. The method writes the fields common across all
     * message types (the MsgType code) followed only by the fields applicable to this specific type.
     * @param out - output stream to write the object to.
     * @throws IOException
     */
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

    /**
     * Deserializes this object from the given input stream. The method reads the message type first
     * then the reads only the fields applicable to this specific type.
     * @param in - input stream to read the object from.
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
    
    /**
     * Returns a string representation of this object. It formats first the common field
     * (the message type) followed only by the fields applicable to the specific message type.
     * @return the string representation of the object.
     * @see MsgTblStone#toString(MsgType)
     */
    @Override
    public String toString() {
        String output = 
              String.format("--[MsgType:%d]----\n", _msgType.ordinal())
            + toString(_msgType);
        return output;
    }
    
    /**
     * Returns a string representation of a subset of the object's fields, the ones
     * applicable to the specific message type given as parameter.
     * @param msgType - the message type to use for filtering the fields to be formatted.
     * @return the string representation of the fields applicable to msgType.
     * @see MsgTblStone#toString()
     */
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
