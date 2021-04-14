package schemas;

public class JsonMember extends JsonStatus {

    public String State;
    public JsonAnswer Answer;
    
    public JsonMember(String ipAddress, String name, String role) {
        super(ipAddress, name, role);
        State = "";
        Answer = null;
    }
}
