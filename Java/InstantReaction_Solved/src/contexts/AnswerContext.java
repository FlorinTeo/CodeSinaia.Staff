package contexts;

import schemas.JsonAnswerContext;

/**
 * Answer Context class, holding all data about an answer.
 * An answer is contextual to a question, identified by the question rolling ID.
 */
public class AnswerContext {
    // Initialize all fields to values reflecting a non-existent answer.
    private int _questionID = 0;
    private String _answerText = "";
    
    /**
     * Gets the Id of the question being answered.
     */
    public int getQuestionID() {
        return _questionID;
    }
    
    /**
     * Gets the text of the answer.
     */
    public String getText() {
        return _answerText;
    }
    
    /**
     * AnswerContext default constructor, modeling an answer which has not been given yet.
     */
    public AnswerContext() {
    }
    
    /**
     * AnswerContext constructor, from the given jsonAnswerContext received from the guest client.
     */
    public AnswerContext(JsonAnswerContext jsonAnswerContext) {
        _questionID = jsonAnswerContext.QuestionID;
        _answerText = jsonAnswerContext.AnswerText;
    }
    
    /**
     * Returns the json container for this answer context.
     */
    public JsonAnswerContext toJson() {
        JsonAnswerContext jsonAnswerContext = new JsonAnswerContext();
        jsonAnswerContext.QuestionID = _questionID;
        jsonAnswerContext.AnswerText = _answerText;
        return jsonAnswerContext;
    }
}
