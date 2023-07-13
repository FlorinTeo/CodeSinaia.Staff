package Regex;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.printf("Regular Expression? > ");
        String regex = console.nextLine();

        do {
            System.out.printf("Text? > ");
            String text = console.nextLine();
            if (text.isEmpty() || text.equalsIgnoreCase("quit")) {
                break;
            }
            if (regexMatch(regex, text)) {
                System.out.printf("'%s' matches regex '%s'\n", text, regex);
            } else {
                System.out.printf("'%s' does NOT match regex '%s'\n", text, regex);
            }
        } while(true);

        System.out.println("Goodbye!");
        console.close();
    }

    private static boolean regexMatch(String regex, String text) {
        int i = 0;
        while (i < Math.min(regex.length(), text.length()) && regex.charAt(i) == text.charAt(i)) {
            i++;
        }

        if (i == regex.length()) {
            return (i == text.length());
        }

        switch(regex.charAt(i)) {
        case '?':
            return (i < text.length()) 
                && regexMatch(regex.substring(i+1), text.substring(i+1));
        case '.':
            return regexMatch(regex.substring(i+1), text.substring(i))
                || (i < text.length() && regexMatch(regex.substring(i+1), text.substring(i+1)));
        case '*':
            for (int j = i; j <= text.length(); j++) {
                if (regexMatch(regex.substring(i+1), text.substring(j))) {
                    return true;
                }
            }
            return false;
        default:
            return false;
        }
    }
}
