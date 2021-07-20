package contexts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server Context class, holding all data shared across all servlets.
 */
public class ServerContext {
    // Dictionary indexing (by their key) all the members, guests or hosts, currently logged in
    private ConcurrentHashMap<String, MemberContext> _audienceMap;
    
    /**
     * ServerContext constructor creates an empty audience map.
     */
    public ServerContext() {
        _audienceMap = new ConcurrentHashMap<String, MemberContext>();
    }
    
    /**
     * Logs the given member returning true on success, false otherwise.
     */
    public boolean loginMember(MemberContext memberContext) {
        if (memberContext == null || _audienceMap.get(memberContext.getKey()) != null) {
            // fail if member is null or is already logged in (present in the map).
            return false;
        } else {
            // all good, add member to the map and return success.
            _audienceMap.put(memberContext.getKey(), memberContext);
            return true;
        }
    }
    
    /**
     * Logs out the given member, returning true on success, false otherwise.
     */
    public boolean logoutMember(MemberContext memberContext) {
        if (memberContext == null || _audienceMap.get(memberContext.getKey()) == null) {
            // fail if member is null or is not currently logged in (not present in the map).
            return false;
        } else {
            // all good, remove member from the map and return success.
            _audienceMap.remove(memberContext.getKey());
            return true;
        }
    }
    
    /**
     * Return a summary of the context on the server:
     * number of members currently logged in and the full list of members.
     */
    @Override
    public String toString() {
        String output = String.format("%d members currently logged in", _audienceMap.size());
        
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            output += String.format("<br>%s", memberContext.toString());
        }
        
        return output;
    }
}
