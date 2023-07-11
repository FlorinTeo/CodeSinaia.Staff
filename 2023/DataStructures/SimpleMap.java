package DataStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimpleMap {
    private static Map<String, Integer> _map = new HashMap<String, Integer>();

    public static void main(String[] args) {
        System.out.println("Hello to Simple Map!");
        Scanner console = new Scanner(System.in);

        do {
            System.out.printf("Command [put/get/remove]? > ");
            String[] line = console.nextLine().split(" ");

            if (line[0].equalsIgnoreCase("quit")) {
                break;
            }

            try {
                switch(line[0].toLowerCase()) {
                case "put":
                    _map.put(line[1], Integer.parseInt(line[2]));
                    System.out.printf("-> [%s]:%s\n", line[1], line[2]);
                    break;
                case "get":
                    System.out.printf("[%s] -> %d\n", line[1], _map.get(line[1]));
                    break;
                case "remove":
                    System.out.printf("%s -> %d\n", line[1], _map.remove(line[1]));
                    break;
                default:
                    throw new RuntimeException("Unsupported operation!");
                }
            } catch(Exception e) {
                System.out.printf("%s\n",e.getClass().toString());
            }
            System.out.printf("map size = %d\n", _map.size());
        } while(true);

        console.close();
        System.out.println("Goodbye!");
    }
}