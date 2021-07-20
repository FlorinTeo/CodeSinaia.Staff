package contexts;

import schemas.JsonMemberContext;

/**
 * GuestContext class extending MemberContext with Guest specific data
 */
public class GuestContext extends MemberContext {
    /**
     * Most recent answer received from the guest. null if there's none.
     */
    private AnswerContext _answerContext;

    /**
     * GuestContext constructor requires the guest IP and guest name
     */
    public GuestContext(String ipAddress, String name) {
        super(ipAddress, name);
        _answerContext = null;
    }
    
    /**
     * Returns the last answer given by this guest.
     */
    public AnswerContext getAnswer() {
        return _answerContext;
    }
    
    /**
     * Stores the answer given by this guest.
     */
    public void setAnswer(AnswerContext answerContext) {
        _answerContext = answerContext;
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
        jsonGuestContext.Answer = (_answerContext != null) ? _answerContext.toJson() : null;
        return jsonGuestContext;
    }
}
