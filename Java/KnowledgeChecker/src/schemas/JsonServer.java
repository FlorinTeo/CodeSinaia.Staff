package schemas;

import java.util.ArrayList;
import java.util.List;

public class JsonServer extends JsonStatus {

    public List<JsonMember> Members;
    
    public JsonServer(String ipAddress) {
        super(ipAddress, "", "");
        Members = new ArrayList<JsonMember>();
    }
}
