package contexts;

public class SpeakerContext extends MemberContext {

    public SpeakerContext(String name, String ipAddress) {
        super(name, ipAddress);
    }
    
    @Override
    public String getRole() {
        return "Speaker";
    }
}