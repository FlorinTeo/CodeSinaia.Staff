package schemas;

import java.util.ArrayList;
import java.util.List;

public class JsonSpeakerStatus extends JsonStatus {
    public List<JsonMember> Members;
    
    public JsonSpeakerStatus(String ipAddress, String name) {
        super(ipAddress, name, "Speaker");
        Members = new ArrayList<JsonMember>();
    }
}
