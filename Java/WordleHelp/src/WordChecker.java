import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordChecker {
    /**
     * Array of 5 Character values.
     * If _greenChars[i] is not null, then that character is expected to be found at same position
     * in the word being tested. If null, it means any character is accepted on that position.
     * Initially, all positions are set to null.
     */
    private Character[] _greenChars;

    /**
     * Set of Character values. The characters in this set are expected to NOT be found anywhere
     * in the word being tested.
     * Initially the set is empty.
     */
    private Set<Character> _blackChars;
    
    /**
     * Map associating a Character with a 5-elements boolean array. For a given character ch,
     * If _orangeChar.get(ch)[i] is true then ch was marked orange for position i in the word.
     * So if the word being tested has ch in the same position it should NOT be considered a match.
     * In addition, all characters in the key set of the map SHOULD be present in the word.
     */
    private Map<Character, boolean[]> _orangeChars;
    
    private enum Marker {
        GREEN('+'),
        ORANGE('~'),
        BLACK('-');
        
        private char _value;
        private static Map<Character, Marker> _map = new HashMap<>();

        static {
            for (Marker marker : Marker.values()) {
                _map.put(marker._value, marker);
            }
        }
        
        private Marker(char value) {
            _value = value;
        }
        
        public static boolean isMarker(char chMarker) {
            return _map.containsKey(chMarker);
        }
        
        public static Marker valueOf(char chMarker) {
            return (Marker) _map.get(chMarker);
        }
    };
    
    public WordChecker() {
        clear();
    }
    
    /**
     * Clears the content of all collections in the checker. This is equivalent to wiping out
     * the history of recorded WORDLE hints.
     */
    public void clear() {
        _greenChars = new Character[WordleHelper.WORDLE_LENGTH];
        _blackChars = new HashSet<Character>();
        _orangeChars = new HashMap<Character, boolean[]>();
    }
    
    /**
     * Checks whether the given word is conforming with all the WORDLE hints
     * as they are captured in the green, black and orange collections.
     * @param word - word to be tested.
     * @return true if the word is conforming with all hints, false otherwise.
     */
    public boolean check(String word) {
        // assume the word is a match (match set to true).
        // change it to false the first time any hint conditions is violated.
        boolean match = true;
        int nOrange = 0;
        
        // go through each character in the word, and keep going as long as
        // the word is still a "match"
        for (int i = 0; match && i < word.length(); i++) {
            Character ch = word.charAt(i);
            
            // check green: if green set has a character, it should match.
            match = match && (_greenChars[i] == null || _greenChars[i] == ch);
            
            // check black: still matching only if ch is not found in the black set.
            match = match && !_blackChars.contains(ch);
            
            // check orange: if ch is found in the orange set, then it
            // better not be true for this position (since it was marked "orange")
            // Also count the matches in the orange set since in the end
            // we need to make sure all from the orange set were found in word.
            if (match && _orangeChars.containsKey(ch)) {
                nOrange++;
                match = !_orangeChars.get(ch)[i];
            }
        }
        
        // the word checks fine only if match is still true at this point
        // and if all characters from the orange set were found in word.
        return match && (nOrange == _orangeChars.size());
    }
    
    /**
     * Update the green, black and orange sets with a new WORDLE hint.
     * The WORDLE hints are in the form of a 2-characters strings.
     * The first character is one of '+'(green), '-'(black) or '~'(orange).
     * I.e: ["-S", "~T", "-A", "+I", "-R" ]
     * The second character is the character itself:
     * @match - WORDLE hints array in the form of five 2-characters strings.
     */
    public void update(String[] match) {
        if (match.length != WordleHelper.WORDLE_LENGTH) {
            throw new InvalidParameterException("Invalid number of hints!");
        }
        
        for (int i = 0; i < match.length; i++) {
            if (match[i].length() != 2 || !Marker.isMarker(match[i].charAt(0))) {
                throw new InvalidParameterException("Invalid hint format!");
            }
            
            Marker marker = Marker.valueOf(match[i].charAt(0));
            char letter = match[i].charAt(1);
            
            switch(marker) {
            case GREEN:
                if (_greenChars[i] != null && _greenChars[i] != letter) {
                    throw new InvalidParameterException("Conflicting green marker!");
                }
                _greenChars[i] = letter;
                break;
            case ORANGE:
                boolean[] positions = _orangeChars.getOrDefault(letter,  new boolean[WordleHelper.WORDLE_LENGTH]);
                positions[i] = true;
                _orangeChars.put(letter, positions);
                break;
            case BLACK:
                _blackChars.add(letter);
                break;
            }
        }
    }
}
