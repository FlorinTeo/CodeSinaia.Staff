package contexts;

import schemas.JsonMemberContext;

/**
 * HostContext class extending MemberContext with Host specific data
 */
public class HostContext extends MemberContext {

    /**
     * HostContext constructor requires the host IP and host name
     */
    public HostContext(String ipAddress, String name) {
        super(ipAddress, name);
        // Host only context fields initialized here
    }

    /**
     * Returns a description summarizing the host member data
     */
    @Override
    public String toString() {
        return String.format("Host %s", super.toString());
    }
    
    /**
     * Returns the json container for this host member.
     */
    @Override
    public JsonMemberContext toJson() {
        JsonMemberContext jsonHostContext = super.toJson();
        jsonHostContext.Role = "Host";
        return jsonHostContext;
    }
}
