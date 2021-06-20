package contexts;

import schemas.JsonAnswer;

public class AnswerContext {
    private QuestionContext _questionContext;
    private String _answerText;
    
    public String getText() {
        return _answerText;
    }
    
    public QuestionContext getQuestion() {
        return _questionContext;
    }
    
    public AnswerContext(QuestionContext questionContext, JsonAnswer jsonAnswer) {
        _questionContext = questionContext;
        _answerText = jsonAnswer.AnswerText;
    }
    
    public JsonAnswer toJsonSchema() {
        JsonAnswer jsonAnswer = new JsonAnswer();
        jsonAnswer.QuestionID = _questionContext.getID();
        jsonAnswer.AnswerText = _answerText;
        return jsonAnswer;
    }
}
