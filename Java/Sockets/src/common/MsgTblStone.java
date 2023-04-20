package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class MsgTblStone implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _from;
    private String _to;
    private char[] _data;
    
    public MsgTblStone(String from, String to, char[] data) {
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
        return new MsgTblStone(from, to, data);
    }
    
    public String getData() {
        return new String(_data);
    }
    
    @Override
    public String toString() {
        String output =
              String.format("   from: %s\n", _from)
            + String.format("     to: %s\n" , _to)
            + String.format("message: \"%s\"\n", new String(_data));
        return output;
    }
}