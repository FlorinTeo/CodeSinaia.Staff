package contexts;

import schemas.JsonMember;

public class AttendantContext extends MemberContext {

    private AnswerContext _answerContext;
    
    public AttendantContext(String name, String ipAddress) {
        super(name, ipAddress);
        _answerContext = null;
    }
    
    public AnswerContext getAnswer() {
        return _answerContext;
    }
    
    public void setAnswer(AnswerContext answerContext) {
        _answerContext = answerContext;
    }
    
    @Override
    public String getRole() {
        return "Attendant";
    }
    
    @Override
    public JsonMember toJsonSchema() {
        JsonMember jsonMember = super.toJsonSchema();
        
        if (_answerContext != null) {
            jsonMember.Answer = _answerContext.toJsonSchema();
        }
        
        return jsonMember;
    }
}
