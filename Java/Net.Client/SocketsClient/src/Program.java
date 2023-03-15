import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program {
    
    private static final String _IP = "127.0.0.1"; //"10.69.112.155";
    private static final int _PORT = 5015;

    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println("Hello to Java Sockets Client!");
        
        Scanner console = new Scanner(System.in);
        
        do {
            System.out.print("Message?> ");
            String message = console.nextLine();
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
            
            System.out.printf("Sending '%s' to %s/%d ... ", message, _IP, _PORT);
            Socket socket = new Socket(_IP, _PORT);
            OutputStream rawStream = socket.getOutputStream();
            DataOutputStream dataStream = new DataOutputStream(rawStream);
            dataStream.writeUTF(message);
            socket.close();
            System.out.println("DONE");

        } while(true);
        
        System.out.println("Goodbye!");
    }
}
