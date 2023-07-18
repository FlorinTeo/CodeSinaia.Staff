package LongestWords;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Program {

    private static final int _N_WORDS = 10;

    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        File f = new File(".");
        System.out.printf("Current folder: %s\n", f.getCanonicalFile());

        do {
            System.out.printf("File name? > ");

            String fileName = console.nextLine();
            if (fileName.isEmpty() || fileName.equalsIgnoreCase("quit")) {
                break;
            }

            try {
                File file = new File(fileName);
                Scanner reader = new Scanner(file);
                String[][] longestWords = getLongestWords(reader);
                printWords(longestWords);
            } catch (Exception e) {
                System.out.printf("##Err: File unreadable!\n");
            }
        } while(true);

        console.close();
    }

    private static String[][] getLongestWords(Scanner reader) {
        String[][] longestWords = new String[_N_WORDS][2];
        int lineNo = 0;
        while(reader.hasNextLine()) {
            lineNo++;
            String line = reader.nextLine();
            String[] words = line.split("[\\s,.!?-]+");
            for (String word : words) {
                if (word.matches(".*[^a-zA-Z].*")) {
                    continue;
                }
                
                for (int i = 0; i < longestWords.length; i++) {
                    if (longestWords[i][1] == null || word.length() > longestWords[i][1].length()) {
                        longestWords[i][0] = "" + lineNo;
                        longestWords[i][1] = word;
                        break;
                    }
                }
            }
        }

        return longestWords;
    }

    private static void printWords(String[][] longestWords) {
        System.out.printf("Longest %d words:\n--------\n", _N_WORDS);
        for(String[] wordInfo : longestWords) {
            System.out.printf("Line:%5s %s\n", wordInfo[0], wordInfo[1]);
        }
    }
}
