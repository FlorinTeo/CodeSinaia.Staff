package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import common.MsgTblStone;

public class Cli_TblStone {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5025;

    public static void main(String[] args) throws UnknownHostException, IOException {
        
        Scanner console = new Scanner(System.in);
        
        do {
            System.out.print("Message?> ");
            String line = console.nextLine();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            
            MsgTblStone message = MsgTblStone.parseMessage(line);
            if (message == null) {
                System.out.println("Invalid message!");
                continue;
            }
            
            System.out.printf("Sending:\n");
            System.out.println(message);
            Socket socket = new Socket(_IP, _PORT);
            ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
            objOutStream.writeObject(message);
            socket.close();
            System.out.println("DONE");

        } while(true);
        
        console.close();
        System.out.println("Client is done! Goodbye!");
    }

}
