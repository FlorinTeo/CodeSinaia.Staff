package Permutations;

import java.util.Scanner;

public class Program {

    private static final int _NUM_PER_ROW = 10;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        do {
            System.out.printf("Text? > ");
            String text = console.nextLine();
            if (text.isEmpty() || text.equalsIgnoreCase("quit")) {
                break;
            }

            int nPrinted = printPermutations("", text, 0);
            System.out.printf("\n---- Total number of permutations : %d ----\n", nPrinted);
        } while(true);

        System.out.println("Goodbye!");
        console.close();
    }

    private static int printPermutations(String prefix, String text, int n) {
        if (text.length() <= 1) {
            n++;
            System.out.printf("%s%s%c", prefix, text, ((n % _NUM_PER_ROW) == 0) ? '\n' : ' ');
        } else {
            for(int i = 0; i < text.length(); i++) {
                n = printPermutations(prefix + text.charAt(i), text.substring(0, i) + text.substring(i+1), n);
            }
        }

        return n;
    }
}
