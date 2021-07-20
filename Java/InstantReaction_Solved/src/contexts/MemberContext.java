package contexts;

import java.time.Duration;
import java.time.LocalDateTime;

import schemas.JsonMemberContext;

/**
 * Member Context abstract class holding data common to Host or Guest members
 */
public abstract class MemberContext {
    private static final int STALE_SECS = 60;
    // Client IP Address of the member
    private String _ipAddress;
    // Member name
    private String _name;
    // Timestamp of the last access
    private LocalDateTime _lastAccessTime;
    
    /**
     * MemberContext constructor requires the member IP and member name
     */
    public MemberContext(String ipAddress, String name) {
        _ipAddress = ipAddress;
        _name = name;
    }
    
    /**
     * Accessor for the member IP.
     */
    public String getIP() {
        return _ipAddress;
    }
    
    /**
     * Accessor for the member name.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Accessor for the key uniquely identifying the member.
     */
    public String getKey() {
        return String.format("%s@%s", _name, _ipAddress);
    }
    
    /**
     * Updates the last access timestamp to the current moment.
     */
    public void touch() {
        _lastAccessTime = LocalDateTime.now();
    }
    
    /**
     * Checks if this member context is stale (hasn't been "touched" in STALE_SECS)
     */
    public boolean isStale() {
        return Duration.between(_lastAccessTime, LocalDateTime.now()).getSeconds() > STALE_SECS;
    }
    
    /**
     * Returns a description summarizing the member data
     */
    @Override
    public String toString() {
        return String.format("%s", getKey());
    }
    
    /**
     * Returns the json container for this member
     */
    public JsonMemberContext toJson() {
        JsonMemberContext jsonMemberContext = new JsonMemberContext();
        jsonMemberContext.IPAddress = _ipAddress;
        jsonMemberContext.Name = _name;
        return jsonMemberContext;
    }
}
