package contexts;

import schemas.JsonQuestionContext;

/**
 * Question Context class, holding all data about a question.
 * A question has a rolling ID, a type and a text. An empty type reflects a non-existent question.
 */
public class QuestionContext {
    // Initialize all fields to values reflecting a non-existent question.
    private String _questionType = "";
    private String _questionText = "";
    
    public String getType() {
        return _questionType;
    }
    
    public String getText() {
        return _questionText;
    }
    
    /**
     * QuestionContext constructor, from the given jsonQuestionContext received from the host client.
     */
    public QuestionContext(JsonQuestionContext jsonQuestionContext) {
        _questionType = jsonQuestionContext.QuestionType;
        _questionText = jsonQuestionContext.QuestionText;
    }
    
    /**
     * Returns the json container for this question context.
     */
    public JsonQuestionContext toJson() {
        JsonQuestionContext jsonQuestionContext = new JsonQuestionContext();
        jsonQuestionContext.QuestionType = _questionType;
        jsonQuestionContext.QuestionText = _questionText;
        return jsonQuestionContext;
    }
}
