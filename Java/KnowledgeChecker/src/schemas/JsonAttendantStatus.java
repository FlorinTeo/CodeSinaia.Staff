package schemas;

public class JsonAttendantStatus extends JsonStatus {
    
    public JsonAttendantStatus(String ipAddress, String name) {
        super(ipAddress, name, "Attendant");
        Question = null;
    }

}
