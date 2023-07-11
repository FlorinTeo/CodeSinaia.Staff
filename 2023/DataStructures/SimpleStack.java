package DataStructures;

import java.util.Scanner;
import java.util.Stack;

public class SimpleStack {
    private static Stack<String> _stack = new Stack<String>();

    public static void main(String[] args) {
        System.out.println("Hello to Simple Stack!");
        Scanner console = new Scanner(System.in);

        do {
            System.out.printf("Command [push/pop/peek]? > ");
            String[] line = console.nextLine().split(" ");

            if (line[0].equalsIgnoreCase("quit")) {
                break;
            }

            switch(line[0].toLowerCase()) {
            case "push":
                if (line.length != 2) {
                    System.out.printf("Missing push argument!\n");
                } else {
                    _stack.push(line[1]);
                    System.out.printf("stack size = %d\n", _stack.size());
                }
                break;
            case "pop":
                if (_stack.isEmpty()) {
                    System.out.printf("(empty)\n");
                } else {
                    System.out.printf("%s\n", _stack.pop());
                    System.out.printf("stack size = %d\n", _stack.size());
                }
                break;
            case "peek":
                if (_stack.isEmpty()) {
                    System.out.printf("(empty)\n");
                } else {
                    System.out.printf("%s\n", _stack.peek());
                    System.out.printf("stack size = %d\n", _stack.size());
                }
                break;
            default:
                System.out.printf("Unsupported operation!\n");
                break;
            }
        } while(true);

        console.close();
        System.out.println("Goodbye!");
    }
}