package schemas;

public class JsonAttendantStatus extends JsonStatus {
    public JsonQuestion Question;
    
    public JsonAttendantStatus(String ipAddress) {
        super(ipAddress);
        Question = null;
    }

}
