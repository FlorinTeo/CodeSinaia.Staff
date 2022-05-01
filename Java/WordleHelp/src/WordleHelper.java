import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordleHelper {

    // WORDLE words have only 5 letters.
    public final static int WORDLE_LENGTH = 5;
    public final static int TOP_N_RESULTS = 100;
    
    // Arguments expected for the > clear command
    private final static String CLEAR_ARG_WORDS = "words";
    private final static String CLEAR_ARG_MATCHES = "matches";
    
    // the map of WORDLE words loaded from the book.
    // Key: is the word itself
    // Value: is the number of occurrences of the word in the book.
    private Map<String, Integer> _wordleWordsMap;
    
    // the engine for keeping track of the sequence of matching hints
    // and for checking individual words against it.
    private WordChecker _wordChecker;
    
    private static boolean hasDistinctLetters(String word) {
    	 // cycles through each letter and its following letters, returning false if they're the same
    	for (int i = 0; i < word.length(); i++) {
    		for (int j = i + 1; j < word.length(); j++) {
        		if (word.charAt(i) == word.charAt(j)) {
        			return false;
        		}
        	}
        }
        return true;
    }
    
    private static boolean hasOnlyLetters(String word) {
        boolean onlyLetters = true;
        for(int i = 0; onlyLetters && i < word.length(); i++) {
            onlyLetters = Character.isLetter(word.charAt(i));
        }
        return onlyLetters;
    }
    
    /**
     * Given a word from the book,
     * returns true if this is a WORDLE word and false otherwise.
     * A WORDLE word has only 5 letters, all different.
     */
    private static boolean isWordleWord(String word) {
        boolean wordleWord = (word.length() == WORDLE_LENGTH)
                && hasOnlyLetters(word)
                && hasDistinctLetters(word);
        return wordleWord;
    }
    
    /**
     * Class constructor:
     * Initialize the WORDLE words database.
     */
    public WordleHelper() {
        _wordleWordsMap = new HashMap<String, Integer>();
        _wordChecker = new WordChecker();
    }
    
    /**
     * Command handler for adding more words to the database, from a given url source
     * @param args - url string pointing to a web page to scrape and load words from.
     * @throws IOException
     */
    public void cmdAdd(String[] args) throws IOException {
        if (args.length != 1) {
            // list of java exceptions:
            // https://programming.guide/java/list-of-java-exceptions.html
            throw new InvalidParameterException("No URL was provided!");
        }
        
        // Create a WebScraper instance for the a book of choice
        WebScraper webScraper = new WebScraper(args[0]);
  
        // Iterate through each word of the book, as it is downloaded from the web
        int nNewWordleWords = 0;
        for(String word : webScraper) {
            word = word.toUpperCase();
            
            // WORDLE words are added to the hash map and counted if they are new
            if (isWordleWord(word)) {
                Integer count = _wordleWordsMap.get(word);
                if (count == null) {
                    count = 0;
                    nNewWordleWords++;
                }
                _wordleWordsMap.put(word,  ++count);
            }
         }
  
        // Print out what we know about this book: its title, how many words it contains,
        // how many of them are WORDLE-type, etc.
        System.out.println(webScraper);
        System.out.printf("Added %d new WORDLE words!\n", nNewWordleWords);
        
        // We're done with the scraper, let's close it.
        webScraper.close();
    }
    
    /**
     * Command handler for clearing either the internal database of words
     * or the history of matches
     * @param args - either of "words" or "matches"
     */
    public void cmdClear(String[] args) {
        if (args.length != 1) {
            // list of java exceptions:
            // https://programming.guide/java/list-of-java-exceptions.html
            throw new InvalidParameterException("Missing {words} or {matches} argument.");
        }
        
        switch(args[0].toLowerCase()) {
        case CLEAR_ARG_WORDS:
            _wordleWordsMap.clear();
            System.out.println("Database of WORDLE words is now empty!");
            break;
        case CLEAR_ARG_MATCHES:
            _wordChecker.clear();
            System.out.println("History of matches is now empty!");
            break;
        default:
            throw new InvalidParameterException(String.format("Argument '%s' not supported", args[0]));
        }
    }
    
    /**
     * Command handler for incorporating more matches into the WordChecker, and
     * print out the words from the dictionary that verify all accumulated matches.
     * @param args - if present it's like ["-H", "~U", "-M", "-A", "+N"]
     * which will be used to update the WordChecker.
     */
    public void cmdMatch(String[] args) {
        if (args.length > 0) {
            _wordChecker.update(args);
        }
        
        int nTop = TOP_N_RESULTS;
        for(Map.Entry<String, Integer> entry :  _wordleWordsMap.entrySet()) {
            String word = entry.getKey();
            if (_wordChecker.check(word)) {
                System.out.println(word);
                if (--nTop == 0) {
                    break;
                }
            }
        }
    }
    
    /**
     * Command handler for saving the content of the WordleWordsMap to a text file.
     * The text is a sequence of lines, each containing the word, tab-separated by its
     * number of occurrences in the map
     * @param args - name of the file to save into.
     * @throws FileNotFoundException 
     */
    public void cmdSave(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new InvalidParameterException("Missing {file_name} to save to.");
        }
        
        PrintStream writer = new PrintStream(new File(args[0]));
        int nLines = 0;
        for(Map.Entry<String, Integer> entry :  _wordleWordsMap.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            writer.printf("%s\t%d\n", word, count);
            nLines++;
            System.out.printf("%c", (nLines % 10) == 0 ? '+' : '.');
            if (nLines % 80 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.printf("Saved %d words to file '%s'\n", nLines, args[0]);
    }
    
    /**
     * Command handler for loading the content of the WordleWordsMap from a text file.
     * The file is expected to be a sequence of text lines, each containing the word,
     * tab-separated by its number of occurrences in the map
     * @param args - name of the file to load from.
     * @throws FileNotFoundException 
     */
    public void cmdLoad(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new InvalidParameterException("Missing {file_name} to load from.");
        }
        
        Scanner reader = new Scanner(new File(args[0]));
        int nLines = 0;
        while(reader.hasNextLine()) {
            String[] wordLine = reader.nextLine().split("\t");
            nLines++;
            String word = wordLine[0];
            Integer count = Integer.parseInt(wordLine[1]);
            Integer crtCount = _wordleWordsMap.get(word);
            if (crtCount != null) {
                crtCount += count;
            }
            _wordleWordsMap.put(word,  count);
            System.out.printf("%c", (nLines % 10) == 0 ? '+' : '.');
            if (nLines % 80 == 0) {
                System.out.println();
            }
        }
        
        reader.close();
        System.out.println();
        System.out.printf("Loaded %d words from file '%s'\n", nLines, args[0]);
    }
    
    public String toString() {
        String result = "";
        // TODO: return a string containing stats about the database
        // How many distinct WORDLE words do we have? Which are the top 10 most frequent ones?
        // See: https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
        return result;
    }
}
