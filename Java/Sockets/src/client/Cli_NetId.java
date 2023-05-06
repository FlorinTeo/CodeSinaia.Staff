package client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

public class Cli_NetId {

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

    public static void main(String[] args) throws SocketException, UnknownHostException {
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
        System.out.printf("Your computer name is '%s'\n", inAddr.getHostName());
        System.out.printf("Your IP address is '%s'\n", Arrays.toString(inAddr.getAddress()));
    }
}
