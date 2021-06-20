package contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schemas.JsonQuestion;

enum QuestionType {
    CHOICE,
    RANGE,
    FREEFORM
}

public class QuestionContext {
    private final Map<String, QuestionType> _questionTypeNameMap = new HashMap<String, QuestionType>() {
        private static final long serialVersionUID = 1L;
        {
            put("Choice", QuestionType.CHOICE);
            put("Range", QuestionType.RANGE);
            put("Free", QuestionType.FREEFORM);
        }
    };
    
    private final Map<QuestionType, String> _questionTypeIDMap = new HashMap<QuestionType, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(QuestionType.CHOICE, "Choice");
            put(QuestionType.RANGE, "Range");
            put(QuestionType.FREEFORM, "Free");
        }
    };
    
    private int _questionID;
    private String _questionText;
    private QuestionType _questionType;
    private List<String> _questionArguments;
    
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
        _questionArguments = new ArrayList<String>();
        String[] args = _questionText.split("\\|");
        for (int i = 1; i < args.length; i+=2) {
            _questionArguments.add(args[i].trim());            
        }
    }
    
    public JsonQuestion toJsonSchema() {
        JsonQuestion jsonQuestion = new JsonQuestion();
        jsonQuestion.QuestionID = _questionID;
        jsonQuestion.QuestionText = _questionText;
        jsonQuestion.QuestionType = _questionTypeIDMap.get(_questionType);
        jsonQuestion.QuestionArguments = _questionArguments.toArray(new String[0]);
        return jsonQuestion;
    }
}
