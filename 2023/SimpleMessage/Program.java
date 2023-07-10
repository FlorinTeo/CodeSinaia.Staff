package SimpleMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.printf("Invalid argument(s): %d\n", args.length);
            return;
        }

        Scanner console = new Scanner(System.in);

        switch(args[0].toLowerCase()) {
        case "read":
            readMessage(console);
            break;
        case "write":
            writeMessage(console);
            break;
        default:
            System.out.printf("Unrecognized argument: \"%s\"",args[0]);
        }

        console.close();
    }

    private static void writeMessage(Scanner console) throws IOException {
        System.out.printf("Message type#?> ");
        int msgType = Integer.parseInt(console.nextLine());
        System.out.printf("Message data?> ");
        String msgData = console.nextLine();
        System.out.printf("File name?> ");
        String fileName = console.nextLine();

        Message msg = new Message(msgType, msgData);
        File file = new File(fileName);
        FileOutputStream fileStream = new FileOutputStream(file);
        ObjectOutputStream outStream = new ObjectOutputStream(fileStream);

        System.out.printf("Writing object: %s\n", msg.toString());
        outStream.writeObject(msg);

        outStream.close();
    }

    private static void readMessage(Scanner console) throws IOException, ClassNotFoundException {
        System.out.printf("File name?> ");
        String fileName = console.nextLine();
        File file = new File(fileName);
        FileInputStream fileStream = new FileInputStream(file);
        ObjectInputStream inStream = new ObjectInputStream(fileStream);
        Message msg = (Message)inStream.readObject();
        System.out.printf("Read message: %s\n", msg.toString());
        inStream.close();
    }
}
