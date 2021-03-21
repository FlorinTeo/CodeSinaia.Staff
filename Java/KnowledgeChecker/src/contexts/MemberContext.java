package contexts;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class MemberContext {
    private String _name;
    private String _ipAddress;
    private LocalDateTime _timeStamp;
    private boolean _active;
    
    public MemberContext(String name, String ipAddress) {
        _name = name;
        _ipAddress = ipAddress;
        _timeStamp = LocalDateTime.now();
        _active = true;
    }
    
    public String getName() {
        return _name;
    }
    
    public String getIP() {
        return _ipAddress;
    }
    
    public boolean checkState(LocalDateTime timeStampNow) {
        boolean prevActive = _active;
        _active = Duration.between(_timeStamp, timeStampNow).getSeconds() < ServerContext.INACTIVE_SECS;
        return prevActive;
    }
    
    public void touch() {
        _timeStamp = LocalDateTime.now();
        _active = true;
    }
    
    public String getState() {
        // DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return _active ? "Active" : "Inactive";
    }
    
    public abstract String getRole();
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("%s:%s@%s", getRole(), _name, _ipAddress));
        return out.toString();
    }
}
