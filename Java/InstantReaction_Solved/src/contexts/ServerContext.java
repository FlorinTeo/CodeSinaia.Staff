package contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import schemas.JsonServerStatus;

/**
 * Server Context class, holding all data shared across all servlets.
 */
public class ServerContext {
    // Dictionary indexing (by their key) all the members, guests or hosts, currently logged in
    private ConcurrentHashMap<String, MemberContext> _audienceMap;
    
    // Counter for questions being asked;
    private int _questionCount;
    
    // Current outstanding question (or null if there's none)
    private QuestionContext _currentQuestion;
    
    /**
     * ServerContext constructor creates an empty audience map a null (cleared) question.
     */
    public ServerContext() {
        _audienceMap = new ConcurrentHashMap<String, MemberContext>();
        _questionCount = 0;
        _currentQuestion = null;
    }
    
    /**
     * Logs the given member returning true on success, false otherwise.
     */
    public boolean loginMember(MemberContext memberContext) {
        if (memberContext == null || _audienceMap.get(memberContext.getKey()) != null) {
            // fail if member is null or is already logged in (present in the map).
            return false;
        } else {
            // all good, add member to the map and return success.
            _audienceMap.put(memberContext.getKey(), memberContext);
            return true;
        }
    }
    
    /**
     * Logs out the given member, returning true on success, false otherwise.
     */
    public boolean logoutMember(MemberContext memberContext) {
        if (memberContext == null || _audienceMap.get(memberContext.getKey()) == null) {
            // fail if member is null or is not currently logged in (not present in the map).
            return false;
        } else {
            // all good, remove member from the map and return success.
            _audienceMap.remove(memberContext.getKey());
            return true;
        }
    }
    
    /**
     * Returns the full member context from the audience map using the key from the given memberContext.
     */
    public MemberContext getMember(MemberContext memberContext) {
        MemberContext memberInAudience = null;
        if (memberContext != null) {
            memberInAudience = _audienceMap.get(memberContext.getKey());
        }
        
        return memberInAudience;
    }
    
    /**
     * Returns the complete list of member contexts.
     */
    public List<MemberContext> getMembersList() {
        List<MemberContext> membersList = new ArrayList<MemberContext>();
        
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            membersList.add(memberContext);
        }
        
        return membersList;
    }
    
    /**
     * Returns the question counter.
     */
    public int getQuestionCount() {
        return _questionCount;
    }
    
    /**
     * Returns the current question, or null if there's none outstanding.
     */
    public QuestionContext getQuestion() {
        return _currentQuestion;
    }
    
    /**
     * Sets the current question to a new one. If the given question is not null
     * it means a new question is being asked (can do it only if the current question is cleared).
     * If the given question is null it means the current question is cleared (it has to exist). 
     * Returns true on success, false otherwise.
     */
    public boolean setQuestion(QuestionContext question) {
        boolean result = false;
        
        if ((question != null) && (_currentQuestion == null)) {
            // setting a new question, since the current question is cleared.
            _currentQuestion = question;
            _questionCount++;
            result = true;
        } else if ((question == null) && (_currentQuestion != null)) {
            // clearing the current outstanding question.
            _currentQuestion = null;
            result = true;
        }
        
        // If a question was set/cleared, reset any previous answer from all guests.
        if (result) {
            for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
                MemberContext memberContext = kvp.getValue();
                if (memberContext instanceof GuestContext) {
                    ((GuestContext)memberContext).setAnswer(null);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Return a summary of the context on the server:
     * number of members currently logged in and the full list of members.
     */
    @Override
    public String toString() {
        String output = String.format("%d members logged in; questions count = %d",
                        _audienceMap.size(), _questionCount);
        
        for (Map.Entry<String, MemberContext> kvp : _audienceMap.entrySet()) {
            MemberContext memberContext = kvp.getValue();
            output += String.format("<br>%s", memberContext.toString());
        }
        
        return output;
    }
    
    /**
     * Returns the json container for the server context
     */
    public JsonServerStatus toJson() {
        JsonServerStatus jsonServerStatus = new JsonServerStatus();
        List<MemberContext> membersList = getMembersList();
        for(MemberContext member : membersList) {
            jsonServerStatus.Members.add(member.toJson());
        }
        
        // Serialize current question, if there is an outstanding one
        if (_currentQuestion != null) {
            jsonServerStatus.Question = _currentQuestion.toJson();
            jsonServerStatus.Question.QuestionID = _questionCount;
        }
        
        jsonServerStatus.Success = true;
        jsonServerStatus.Message = String.format("Current server status: %d members logged in; questions count = %d.", 
                    membersList.size(), _questionCount);
        return jsonServerStatus;
    }
}
