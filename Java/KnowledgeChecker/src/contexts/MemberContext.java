package contexts;

import java.time.Duration;
import java.time.LocalDateTime;

import schemas.JsonMember;

public abstract class MemberContext {
    private String _ipAddress;
    private String _name;
    private LocalDateTime _timeStamp;
    private boolean _active;
    
    public MemberContext(String name, String ipAddress) {
        _name = name;
        _ipAddress = ipAddress;
        _timeStamp = LocalDateTime.now();
        _active = true;
    }
    
    public String getIP() {
        return _ipAddress;
    }
    
    public String getName() {
        return _name;
    }
    
    public static String getKey(String name, String ipAddress) {
        return name + "@" + ipAddress;
    }
    
    public String getKey() {
        return MemberContext.getKey(_name, _ipAddress);
    }
    
    public boolean checkState(LocalDateTime timeStampNow) {
        boolean prevActive = _active;
        _active = Duration.between(_timeStamp, timeStampNow).getSeconds() < ServerContext.INACTIVE_SECS;
        return prevActive;
    }
    
    public boolean checkExpired(LocalDateTime timeStampNow) {
        return !_active
                && Duration.between(_timeStamp, timeStampNow).getSeconds() > ServerContext.EXPIRED_SECS;
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
    
    public JsonMember toJsonSchema() {
        JsonMember jsonMember = new JsonMember(getIP(), getName(), getRole());
        jsonMember.State = _active ? "Active" : "Inactive";
        return jsonMember;
    }
}
