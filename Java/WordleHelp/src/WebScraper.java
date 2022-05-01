import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class for iterating over the words of a book downloaded from the web.
 * The book is expected to be in plain text format which can be read and parsed
 * line by line. One of the lines is expected to list the title of the book,
 * like: "Title: The Time Machine"
 * Words can be of any kinds, hyphenated, with small and large caps, etc.
 * Class implements both Iterator and Iterable interfaces, allowing it to be
 * used in a simple for-each loop.
 */
public class WebScraper implements Iterator<String>, Iterable<String>, Closeable {
    // the special prefix of the line giving the book's title.
    private final static String TITLE_TAG = "Title: ";
    
    // the scanner set-up for parsing the content of the book.
    private Scanner _urlReader;
    
    // the line of text last read from the book. Initially ""
    private String _crtLine;
    
    // the title of the book, as it has been parsed from the book text
    private String _title;
    
    // the total count of lines in the book
    private int _nLines;
    
    // the total count of words read from the book
    private int _nWords;
    
    /**
     * Given a line from the book,
     * returns true if this is the line containing the book title and false otherwise.
     */
    private boolean isTitleLine(String line) {
        return line.contains(TITLE_TAG);
    }
    
    /**
     * Class constructor: constructs and retains the URL object for the
     * given urlLink string, then resets the internal state.
     * @param url - string pointing to the book on the web "https://www.gutenberg.org/files/35/35-0.txt"
     * @throws IOException 
     */
    public WebScraper(String urlLink) throws IOException {
        // use the url link to connect to the web and setup the urlReader.
        URL url = new URL(urlLink);
        URLConnection urlConnection = url.openConnection();
        _urlReader = new Scanner(urlConnection.getInputStream());
        
        // initially no title has been detected and 
        _title = "(unknown)";
        _crtLine = "";
        _nLines = 0;
        _nWords = 0;
    }

    /**
     * Returns true if there are more words to be returned by this iterator, false otherwise.
     */
    @Override // Iterable<>.hasNext()
    public boolean hasNext() {
        return !_crtLine.isEmpty() || _urlReader.hasNextLine();
    }

    /**
     * Returns the next word in the book. This is called by the for-each loop after hasNext
     * returned true, so it's expected either _crtLine is not empty or there's another
     * line to be loaded from the book reader.
     */
    @Override // Iterable<>.next()
    public String next() {
        if (_crtLine.isEmpty()) {
            _crtLine = _urlReader.nextLine();
            _nLines++;
            if (isTitleLine(_crtLine)) {
                _title = _crtLine.substring(TITLE_TAG.length());
            }
        }
        
        String[] wordBreak = _crtLine.split(" ", 2);
        _crtLine = wordBreak.length > 1 ? wordBreak[1] : "";
        _nWords++;
        return wordBreak[0];
    }

    /**
     * Returns this instance as an iterator.
     * This enables using this object from within the for-each loop.
     */
    @Override // Iterator<>.iterator()
    public Iterator<String> iterator() {
        return this;
    }

    /**
     * This class embeds a scanner which is a resource needing to be closed.
     * As such, this object itself needs to be closed, so it implements the Closeable interface.
     */
    @Override // Closeable.close()
    public void close() throws IOException {
        _urlReader.close();
    }
    
    /**
     * A String representation of this object: Statistics about the book
     */
    @Override // Object.toString()
    public String toString() {
        String result = "";
        result += String.format("Title: %s\n", _title);
        result += String.format("Number of lines: %s\n", _nLines);
        result += String.format("Number of words: %s", _nWords);
        return result;
    }
}
