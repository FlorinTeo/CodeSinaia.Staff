package contexts;

public class AttendantContext extends MemberContext {

	public AttendantContext(String name, String ipAddress) {
		super(name, ipAddress);
	}
	
	@Override
	public String getRole() {
		return "Attendant";
	}
}
