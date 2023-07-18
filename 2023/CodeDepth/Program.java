package CodeDepth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.printf("Current folder: %s\n", (new File(".")).getAbsolutePath());
        System.out.printf("Code file? > ");
        String codeFilename = console.nextLine();

        File codeFile = new File(codeFilename);
        int codeDepth = calculateDepth(codeFile);
        System.out.printf("Code depth of '%s' : %d\n", codeFile.getName(), codeDepth);

        System.out.println("Goodbye!");
        console.close();
    }

    private static int calculateDepth(File codeFile) throws FileNotFoundException {
        Scanner codeReader = new Scanner(codeFile);

        int crtDepth = 0;
        int codeDepth = 0;
        
        while(codeReader.hasNextLine()) {
            Scanner lineReader = new Scanner(codeReader.nextLine());
            while(lineReader.hasNext()) {
                String token = lineReader.next();
                for(char ch : token.toCharArray()) {
                    if (ch == '{') {
                        crtDepth++;
                        codeDepth = Math.max(crtDepth, codeDepth);
                    } else if (ch == '}') {
                        crtDepth--;
                    }
                }
            }
        }
        codeReader.close();
        return codeDepth;
    }
}
