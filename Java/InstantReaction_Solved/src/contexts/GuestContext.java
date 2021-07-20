package contexts;

import schemas.JsonMemberContext;

/**
 * GuestContext class extending MemberContext with Guest specific data
 */
public class GuestContext extends MemberContext {

    /**
     * GuestContext constructor requires the guest IP and guest name
     */
    public GuestContext(String ipAddress, String name) {
        super(ipAddress, name);
        // Guest only context fields initialized here
    }
    
    /**
     * Returns a description summarizing the guest member data
     */
    @Override
    public String toString() {
        return String.format("Guest %s", super.toString());
    }
    
    /**
     * Returns the json container for this guest member.
     */
    @Override
    public JsonMemberContext toJson() {
        JsonMemberContext jsonGuestContext = super.toJson();
        jsonGuestContext.Role = "Guest";
        return jsonGuestContext;
    }

}
