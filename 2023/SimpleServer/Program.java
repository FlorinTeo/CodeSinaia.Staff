package SimpleServer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import SimpleMessage.Message;

public class Program {
    private static Map<String, Queue<Message>> _map = new HashMap<String, Queue<Message>>();

    public static void main(String[] args) {
        System.out.println("Hello to Simple Server!");
        Scanner console = new Scanner(System.in);

        do {
            System.out.printf("Command [login/logout/send/receive]? > ");
            String[] line = console.nextLine().split(" ");

            if (line[0].equalsIgnoreCase("quit")) {
                break;
            }

            try {
                switch(line[0].toLowerCase()) {
                case "login":
                    processLogin(line[1]);
                    break;
                case "logout":
                    processLogout(line[1]);
                    break;
                case "send":
                    Message message = new Message(0, line[2]);
                    processSend(line[1], message);
                    break;
                case "receive":
                    message = processReceive(line[1]);
                    if (message == null) {
                        System.out.printf("(no new messages)\n");
                    } else {
                         System.out.printf("%s\n",message.toString());
                    }
                    break;
                default:
                    throw new RuntimeException("Unsupported operation!");
                }
            } catch(Exception e) {
                System.out.printf("%s:%s\n",
                    e.getClass().toString(),
                    e.getMessage());
            }
            showState();
        } while(true);

        console.close();
        System.out.println("Goodbye!");
    }

    private static void processLogin(String name) {
        if (_map.containsKey(name)) {
            throw new RuntimeException("Already logged in!");
        }

        _map.put(name, new LinkedList<Message>());
    }

    private static void processLogout(String name) {
        if (!_map.containsKey(name)) {
            throw new RuntimeException("Not logged in!");
        }

        _map.remove(name);
    }

    private static void processSend(String toName, Message message) {
        if (!_map.containsKey(toName)) {
            throw new RuntimeException("Unknown recipient!");
        }

        Queue<Message> queue = _map.get(toName);
        queue.add(message);
    }

    private static Message processReceive(String name) {
        if (!_map.containsKey(name)) {
            throw new RuntimeException("Unknown requester!");
        }

        Queue<Message> queue = _map.get(name);
        return queue.isEmpty() ? null : queue.remove();
    }

    private static void showState() {

        System.out.printf("----Logged in: %d user(s)\n", _map.size());
        for(Map.Entry<String, Queue<Message>> kvp : _map.entrySet()) {
            System.out.printf("    %s : %d message(s).\n",
                kvp.getKey(),
                kvp.getValue().size());
        }
    }
}