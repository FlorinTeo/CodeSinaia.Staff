package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

import common.MsgNetId;

public class Cli_NetId {
    
    private static final String SRV_NETID_IP = "127.0.0.1"; // "10.84.160.100";
    private static final int SRV_NETID_PORT = 5025;

    @SuppressWarnings("unused")
    private static void netInfo() throws SocketException {
        Enumeration<NetworkInterface> netIfs = NetworkInterface.getNetworkInterfaces();
        while (netIfs.hasMoreElements()) {
            NetworkInterface netIf = netIfs.nextElement();
            if (!netIf.isUp()) {
                continue;
            }

            Enumeration<InetAddress> inAddrs = netIf.getInetAddresses();
            boolean hasInAddr = false;
            while (inAddrs.hasMoreElements()) {
                if (!hasInAddr) {
                    System.out.printf("%s [up:%b][virtual:%b][loopback:%b][p2p:%b]:\n", netIf.getDisplayName(),
                            netIf.isUp(), netIf.isVirtual(), netIf.isLoopback(), netIf.isPointToPoint());
                    hasInAddr = true;
                }
                InetAddress inAddr = inAddrs.nextElement();
                System.out.printf("    %s\n", inAddr);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String name;
        if (args.length > 0) {
            name = args[0];

        } else {
            System.out.printf("Hello!\nLet's choose a name> ");
            Scanner input = new Scanner(System.in);
            name = input.nextLine();
            input.close();
        }
        System.out.printf("Hello '%s'\n", name);
        InetAddress inAddr = InetAddress.getLocalHost();
        MsgNetId msgNetId = new MsgNetId(name, inAddr);
        System.out.println(msgNetId);
        
        Socket socket = new Socket(SRV_NETID_IP, SRV_NETID_PORT);
        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
        objOutStream.writeObject(msgNetId);
        socket.close();
        System.out.println("Registered and .. Goodbye!");
    }
}
