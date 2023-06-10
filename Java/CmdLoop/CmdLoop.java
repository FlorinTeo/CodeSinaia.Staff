package Java.CmdLoop;

import java.util.Scanner; 

public class CmdLoop { 

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("help|?        : this help content.");
        System.out.println("quit|exit|q|x : exits the command loop.");
    }

    public static void main(String[] args) {
        System.out.println("This is a typical command loop..."); 
  
        Scanner console = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            System.out.print("Command [help] ?> ");
            String line = console.nextLine();
            switch(line.toLowerCase()) {
                case "q":
                case "x":
                case "quit":
                case "exit":
                    done = true;
                    break;
                case "?":
                case "help":
                    printHelp();
                    break;
            }
        }

        System.out.println("Goodbye!");
        console.close();
    }
}