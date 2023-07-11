package DataStructures;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SimpleQueue {
    private static Queue<String> _queue = new LinkedList<String>();

    public static void main(String[] args) {
        System.out.println("Hello to Simple Queue!");
        Scanner console = new Scanner(System.in);

        do {
            System.out.printf("Command [add/delete]? > ");
            String[] line = console.nextLine().split(" ");

            if (line[0].equalsIgnoreCase("quit")) {
                break;
            }

            switch(line[0].toLowerCase()) {
            case "add":
                if (line.length != 2) {
                    System.out.printf("Missing add argument!\n");
                } else {
                    _queue.add(line[1]);
                }
                break;
            case "remove":
                if (_queue.isEmpty()) {
                    System.out.printf("(empty)\n");
                } else {
                    System.out.printf("%s\n", _queue.remove());
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