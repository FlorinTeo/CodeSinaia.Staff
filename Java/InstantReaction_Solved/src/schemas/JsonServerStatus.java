package schemas;

import java.util.ArrayList;
import java.util.List;

public class JsonServerStatus extends JsonStatus {
    public List<JsonMemberContext> Members = new ArrayList<JsonMemberContext>();
}
