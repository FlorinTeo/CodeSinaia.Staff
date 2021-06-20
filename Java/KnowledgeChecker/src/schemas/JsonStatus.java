package schemas;

public class JsonStatus {
    public boolean Success;
    public String Message;
    public String IPAddress;
    public String Name;
    public String Role;
    public JsonQuestion Question;

    public JsonStatus(String ipAddress, String name, String role) {
        this.Success = true;
        this.Message = "OK";
        this.IPAddress = ipAddress;
        this.Name  = name;
        this.Role = role;
        this.Question = null;
    }
    
    public void Assert(boolean condition, String errorMessage) {
        Success = condition;
        if (!condition) {
            Message = errorMessage;
        }
    }
}
