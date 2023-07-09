package HelloSockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloSocketsSrv {
    private static final int SERVER_PORT = 5025;

    public static void main(String[] args) throws Exception {
        System.out.println("Hello to Sockets Server!");

        // Create a server socket and bind it to the given port, then wait for clients to write to it.
        ServerSocket srv = new ServerSocket(SERVER_PORT);
        Socket s = srv.accept();

        // Get the incoming stream on the connecting socket and read whatever the client sent.
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        String message = (String)is.readObject();
        System.out.println(message);
        // Create the responding message matching whatever the client sent.
        if (message.equalsIgnoreCase("Ping!")) {
            message = "Pong!";
        } else {
            message = "Huh?";
        }
        // Get the outgoing stream and write the response message to it.
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(message);
        // Close the connection socket.
        s.close();

        // Close the server socket.
        srv.close();

        System.out.println("Goodbye!");
    }
}
