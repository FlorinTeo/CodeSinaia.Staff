package Java.CmdLoop;

import java.util.Scanner; 

public class CmdLoop { 
    public static void main(String[] args) {
        System.out.println("This is wordle help..."); 
        System.out.print("Command?> ");
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
    
        while(!line.equalsIgnoreCase("exit") && !line.equalsIgnoreCase("quit")) 
        {
            if (line.isEmpty()) {
                switch(line) {
                    case "add":
                        System.out.println("Processing add");
                        break;
                    case "match":
                        System.out.println("Processing match");
                        break;
                    default:
                        System.out.println("Command unrecognized");
                }
            }
        }
        System.out.println(line);
        System.out.print("Command?> ");
        line = input.nextLine();
    }
        
}