package contexts;

import java.util.HashMap;
import java.util.Map;

import schemas.JsonMember;
import schemas.JsonQuestion;

enum QuestionType {
    YESNO,
    TRUEFALSE,
    NUMERIC,
    FREEFORM
}

public class QuestionContext {
    private final Map<String, QuestionType> _questionTypeNameMap = new HashMap<String, QuestionType>() {{
        put("YesNo", QuestionType.YESNO);
        put("TrueFalse", QuestionType.TRUEFALSE);
        put("Numeric", QuestionType.NUMERIC);
        put("Free", QuestionType.FREEFORM);
    }};
    
    private final Map<QuestionType, String> _questionTypeIDMap = new HashMap<QuestionType, String>() {{
        put(QuestionType.YESNO, "YesNo");
        put(QuestionType.TRUEFALSE, "TrueFalse");
        put(QuestionType.NUMERIC, "Numeric");
        put(QuestionType.FREEFORM, "Free");
    }};
    
    private int _questionID;
    private String _questionText;
    private QuestionType _questionType;
    
    public int getID() {
        return _questionID;
    }
    
    public String getText() {
        return _questionText;
    }
    
    public QuestionType getType() {
        return _questionType;
    }
    
    public QuestionContext(JsonQuestion jsonQuestion) {
        _questionID = jsonQuestion.QuestionID;
        _questionText = jsonQuestion.QuestionText;
        _questionType = _questionTypeNameMap.get(jsonQuestion.QuestionType);
    }
    
    public JsonQuestion toJsonSchema() {
        JsonQuestion jsonQuestion = new JsonQuestion();
        jsonQuestion.QuestionID = _questionID;
        jsonQuestion.QuestionText = _questionText;
        jsonQuestion.QuestionType = _questionTypeIDMap.get(_questionType);
        return jsonQuestion;
    }
}
