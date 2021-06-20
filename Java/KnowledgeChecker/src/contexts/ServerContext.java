package contexts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import schemas.JsonMember;
import schemas.JsonServer;

public class ServerContext extends TimerTask {

    public static final long HEARTBEAT_MS = 5000;
    public static final int INACTIVE_SECS = 60;
    public static final int EXPIRED_SECS = 120;
    
    private ConcurrentMap<String, MemberContext> _audienceMap;
    private QuestionContext _questionContext;
    private int _tickCount;
    private int _questionCount;
    private Timer _heartBeat;
    
    public ServerContext() {
        _audienceMap = new ConcurrentHashMap<String, MemberContext>();
        _questionContext = null;
        _tickCount = 0;
        _questionCount = 0;
        _heartBeat = new Timer();
        _heartBeat.schedule(this, HEARTBEAT_MS, HEARTBEAT_MS);
    }
    
    public int tickCount() {
        _tickCount++;
        return _tickCount;
    }
    
    public int nextQuestionID() {
        _questionCount++;
        return _questionCount;
    }
    
    public boolean setQuestion(QuestionContext questionContext) {
        boolean success = ((_questionContext == null) && (questionContext != null))
                       || ((_questionContext != null) && (questionContext == null));
        
        if (success) {
            _questionContext = questionContext;
        }
        
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            // since question  is changing reset all answers across all attendees
            if (memberContext instanceof AttendantContext) {
                ((AttendantContext)memberContext).setAnswer(null);
            }
        }
        
        return success;
    }
    
    public QuestionContext getQuestion() {
        return _questionContext;
    }
    
    public MemberContext getMember(String memberKey) {
        tickCount();
        return _audienceMap.get(memberKey);
    }
    
    public MemberContext loginMember(MemberContext memberContext) {
        if (getMember(memberContext.getKey()) == null) {
            _audienceMap.put(memberContext.getKey(), memberContext);
        } else {
            memberContext = null;
        }
        
        return memberContext;
    }
    
    public MemberContext logoutMember(String ipAddress) {
        MemberContext memberContext = getMember(ipAddress);
        if (memberContext != null) {
            _audienceMap.remove(memberContext.getKey());
        }
        
        return memberContext;
    }
    
    public JsonServer toJsonSchema(String ipAddress) {
        JsonServer jsonServer = new JsonServer(ipAddress);
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            JsonMember jsonMember = memberContext.toJsonSchema();
            jsonServer.Members.add(jsonMember);
        }
        
        return jsonServer;
    }
    
    public void closing() {
        _heartBeat.cancel();
    }
    
    @Override
    public void run() {
        LocalDateTime timeNow = LocalDateTime.now();
        List<MemberContext> expiredMembers = new ArrayList<MemberContext>();
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            memberContext.checkState(timeNow);
            if (memberContext.checkExpired(timeNow)) {
                expiredMembers.add(memberContext);
            }
        }
        
        for (MemberContext expiredMember : expiredMembers) {
            _audienceMap.remove(expiredMember.getKey());
        }
        
        _tickCount++;
    }
}
