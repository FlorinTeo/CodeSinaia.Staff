import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Scanner;

public class Program {
    
    /**
     * Some URLs to a few books from Project Gutenberg
     * (https://www.gutenberg.org/ebooks/search/?sort_order=downloads)
     * 
     * The Time Machine [H. G. Wells]: https://www.gutenberg.org/files/35/35-0.txt
     * Peter Pan [James M. Barrie]: https://www.gutenberg.org/files/16/16-0.txt
     * The Picture of Dorian Gray [Oscar Wilde]: https://www.gutenberg.org/cache/epub/174/pg174.txt
     * The Prince and the Pauper [Mark Twain]: https://www.gutenberg.org/files/1837/1837-0.txt
     * The Motor Ranges on Blue Water [Marvin West]: https://www.gutenberg.org/cache/epub/67899/pg67899.txt
     */
    
    private static final String WORDLE_CMD_QUIT = "quit";
    private static final String WORDLE_CMD_ADD = "add";
    private static final String WORDLE_CMD_CLEAR = "clear";
    private static final String WORDLE_CMD_MATCH = "match";
    private static final String WORDLE_CMD_SAVE = "save";
    private static final String WORDLE_CMD_LOAD = "load";
    private static final String WORDLE_CMD_HELP = "?";
    
    /**
     * Prints a summary description of all supported commands
     */
    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.printf("    add {url}:\n\t%s\n\t%s\n",
                "add words from the given url.",
                "i.e> add https://www.gutenberg.org/files/16/16-0.txt");
        System.out.printf("    clear {words|matches}:\n\t%s\n",
                "clears the database of words or the past matches");
        System.out.printf("    match {pattern}:\n\t%s\n\t%s\n\t%s\n",
                "matches a given pattern against the database and prints top results.",
                "i.e> match ~H+U-M-A-N",
                "where WORDLE hints are ~(Orange), +(Green) and -(Black)");
        System.out.printf("    save {file_name}:\n\t%s\n",
                "saves the wordle database to a text file.");
        System.out.printf("    load {file_name}:\n\t%s\n",
                "loads the wordle database from a text file.");
        System.out.printf("    quit:\n\t%s\n",
                "quits WORDLE Helper.");
    }
    
    /**
     * Main method of the project. It implements a command loop for the following commands:
     * > load: loads a book from the internet, given its URL to www.gutenberg.org
     * > match: provides a sequence of wordle hints. The program remembers them and provides
     *   back the top list of words matching all the hints.
     * > clear: allows resetting the word database or the list of hints.
     * > ?: prints a short description of these commands
     * > quit: exits the program.
     * 
     * Example:
     *     Let me help you with today's WORDLE!
     *     Loading book ... DONE
     *     WORDLE Helper? > add https://www.gutenberg.org/files/16/16-0.txt
     *        Added # WORDLE words to the database
     *     WORDLE Helper? > match
     *        ROUND
     *        BLEAK
     *        FROST
     *     WORDLE Helper? > match -R -O ~U -N -D
     *        TULIP
     *        ULTRA
     *        ...
     *     WORDLE Helper? > match -T +U -L -I -P
     *        HUMAN
     *        CURVE
     *        ...
     *     WORDLE Helper? > match ~H +U -M -A -N
     *        CUSHY
     *        ...
     * The markers "-, ~, +" in the pattern relate to the colors black, orange and green
     * in the WORDLE puzzle. The program is smart enough to build more context as more words
     * are being tried.
     */
    public static void main(String[] args) {
        
        System.out.println("Let me help you with today's WORDLE!");
        WordleHelper wordleHelper = new WordleHelper();
        Scanner input = new Scanner(System.in);
        
        do {
            try {
                System.out.print("WORDLE Helper? > ");
                String cmdLine = input.nextLine().trim();
                
                // empty or space inputs are no-op
                if (cmdLine.isEmpty()) {
                    continue;
                }
                
                // we have at least one word. Extract the command from its arguments.
                String[] cmdLineParts = cmdLine.split(" ");
                String cmd = cmdLineParts[0].toLowerCase();
                String[] cmdArgs = Arrays.copyOfRange(cmdLineParts, 1, cmdLineParts.length);
                
                // "quit" just breaks the loop
                if (cmd.equals(WORDLE_CMD_QUIT)) {
                    break;
                }
                
                // use wordleHelper to process them as needed
                switch(cmd) {
                case WORDLE_CMD_HELP:
                    printHelp();
                    break;
                case WORDLE_CMD_ADD:
                    wordleHelper.cmdAdd(cmdArgs);
                    break;
                case WORDLE_CMD_CLEAR:
                    wordleHelper.cmdClear(cmdArgs);
                    break;
                case WORDLE_CMD_MATCH:
                    wordleHelper.cmdMatch(cmdArgs);
                    break;
                case WORDLE_CMD_SAVE:
                    wordleHelper.cmdSave(cmdArgs);
                    break;
                case WORDLE_CMD_LOAD:
                    wordleHelper.cmdLoad(cmdArgs);
                    break;
                default:
                    System.out.printf("Command '%s' is not supported\n", cmd);
                    break;
                }
            } catch (IOException|InvalidParameterException e) {
                // Anything going wrong, like web page being unreachable, or failure in parsing
                // is printed along with the code location of the failure.
                System.out.printf("ERROR: %s\n", e.getMessage());
            }
        } while(true);
        
        System.out.println("Goodbye!");
        input.close();
    }
}
