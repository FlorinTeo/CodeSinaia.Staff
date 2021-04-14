package schemas;

public class JsonStatus {
    public boolean Success;
    public String IPAddress;
    public String Name;
    public String Role;
    public String Message;

    public JsonStatus(String ipAddress, String name, String role) {
        this.Success = true;
        this.IPAddress = ipAddress;
        this.Name  = name;
        this.Role = role;
        this.Message = "OK";
    }
    
    public JsonStatus(String ipAddress) {
        this(ipAddress, "", "");
    }

    public void Assert(boolean condition, String errorMessage) {
        Success = condition;
        if (!condition) {
            Message = errorMessage;
        }
    }
}
