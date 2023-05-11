package common;

import java.io.Serializable;

public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;

    private MsgCode _msgCode;
    private String _from;
    private String _to;
    private char[] _data;
    private String _status;
    
    public MsgTblStone(MsgCode msgCode) {
        _msgCode = msgCode;
    }
    
    public MsgTblStone(MsgCode msgCode, String from, String to, char[] data) {
        this(msgCode);
        if (data.length != 6) {
            throw new RuntimeException("Invalid data size!");
        }
        _data = data;
        _from = from;
        _to = to;
    }
    
    public static MsgTblStone parseMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length != 3
            || !parts[0].startsWith("from:")
            || !parts[1].startsWith("to:")
            || parts[2].length() != 6) {
            return null;
        }
        String from = parts[0].substring("from:".length());
        String to = parts[1].substring("to:".length());
        char[] data = parts[2].toCharArray();
        return new MsgTblStone(MsgCode.Send, from, to, data);
    }
    
    public String getData() {
        return new String(_data);
    }
    
    @Override
    public String toString() {
        String output =
              String.format("  [From: %s]\n", _from)
            + String.format("  [To: %s]\n" , _to)
            + String.format("  \"%s\"\n", new String(_data));
        return output;
    }
}
