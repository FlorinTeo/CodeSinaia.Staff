package contexts;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerContext extends TimerTask {

	public static final long HEARTBEAT_MS = 5000;
	public static final int INACTIVE_SECS = 60;
	
	private ConcurrentMap<String, MemberContext> _audienceMap;
	private int _tickCount;
	private Timer _heartBeat;
	
	public ServerContext() {
		_audienceMap = new ConcurrentHashMap<String, MemberContext>();
		_tickCount = 0;
		_heartBeat = new Timer();
		_heartBeat.schedule(this, HEARTBEAT_MS, HEARTBEAT_MS);
	}
	
	public int tickCount() {
		_tickCount++;
		return _tickCount;
	}
	
	public MemberContext getMember(String ipAddres) {
		tickCount();
		return _audienceMap.get(ipAddres);
	}
	
	public MemberContext loginMember(MemberContext memberContext) {
		if (getMember(memberContext.getIP()) == null) {
			_audienceMap.put(memberContext.getIP(), memberContext);
		} else {
			memberContext = null;
		}
		
		return memberContext;
	}
	
	public MemberContext logoutMember(String ipAddress) {
		MemberContext memberContext = getMember(ipAddress);
		if (memberContext != null) {
			_audienceMap.remove(memberContext.getIP());
		}
		
		return memberContext;
	}
	
	public void closing() {
		_heartBeat.cancel();
	}
	
	private String mapToHtml() {
		StringBuilder output = new StringBuilder();
		
		if (_audienceMap.size() > 0) {
			output.append("<table><tr>");
			output.append("<th width=20%>IP</th>");
			output.append("<th>Name</th>");
			output.append("<th width=25%>Role</th>");
			output.append("<th width=15%>State</th>");
			output.append("</tr>");
			for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
				MemberContext memberContext = kvp.getValue();
				output.append("<tr>");
				output.append(String.format("<td>%s</td>", memberContext.getIP()));
				output.append(String.format("<td>%s</td>", memberContext.getName()));
				output.append(String.format("<td>%s</td>", memberContext.getRole()));
				output.append(String.format("<td>%s</td>", memberContext.getState()));
				output.append("</tr>");
			}
			output.append("</table>");
		}
		
		return output.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		
		output.append("ServerContext:<br>");
		output.append(String.format("&nbsp&nbsp&nbsp&nbsp_counter: [%d]<br>", _tickCount));
		output.append(String.format("&nbsp&nbsp&nbsp&nbsp_audienceMap: [%d]<br>", _audienceMap.keySet().size()));
		output.append("<p>");
		output.append(mapToHtml());
		
		_tickCount++;
		return output.toString();
	}

	@Override
	public void run() {
		LocalDateTime timeNow = LocalDateTime.now();
		for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
			MemberContext memberContext = kvp.getValue();
			memberContext.checkState(timeNow);
		}
		
		_tickCount++;
	}
}
