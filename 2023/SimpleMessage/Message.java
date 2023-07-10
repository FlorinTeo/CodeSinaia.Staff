package SimpleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable {

    private int _msgType;

    private String _msgData;

    public Message(int msgType, String msgData) {
        _msgType = msgType;
        _msgData = msgData;
    }

    @Override
    public String toString() {
        return String.format("{type:%d, data:\"%s\"}", _msgType, _msgData);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(_msgType);
        out.writeObject(_msgData);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        _msgType = (int)in.readObject();
        _msgData = (String)in.readObject();
    }
}