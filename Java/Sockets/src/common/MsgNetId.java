package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Arrays;

public class MsgNetId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _name;
    private InetAddress _netAddr;
    
    public MsgNetId(String name, InetAddress netAddr) {
        _name = name;
        _netAddr = netAddr;
    }
    
    private String ipToStr(byte[] ip) {
        String output = "";
        for(byte b : ip) {
            output += "." + (b & 0xff);
        }
        return output.substring(1);
    }
    
    @Override
    public String toString() {
        String output =
              String.format("  [%s@:%s]\n", _name, ipToStr(_netAddr.getAddress()));
        return output;
    }
}
