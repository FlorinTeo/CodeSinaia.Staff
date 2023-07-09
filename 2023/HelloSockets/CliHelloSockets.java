package HelloSockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CliHelloSockets {
    private static final int SERVER_PORT = 5025;
    private static final String SERVER_IP = "127.0.0.1";
    public static void main(String[] args) throws Exception {
        System.out.println("Hello to Sockets Client!");

        // Create a connection socket to the given server, on the given port
        Socket s = new Socket(SERVER_IP, SERVER_PORT);
        // Get its outgoing stream and write a simple string message to it
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject("Ping!");
        // Then get its incoming stream and read whatever the server sent on it
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        String message = (String)is.readObject();
        System.out.println(message);
        // In the end cleanup resources
        s.close();

        System.out.println("Goodbye!");
    }
}
