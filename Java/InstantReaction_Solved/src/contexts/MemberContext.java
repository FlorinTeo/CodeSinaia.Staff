package contexts;

import schemas.JsonMemberContext;

/**
 * Member Context abstract class holding data common to Host or Guest members
 */
public abstract class MemberContext {
    // Client IP Address of the member
    private String _ipAddress;
    // Member name
    private String _name;
    
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
