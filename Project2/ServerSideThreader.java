import java.io.*;
import java.net.*;

public class ServerSideThreader {
    public static void main(String[] args) {
        //args[0] = portListen;
        if (args.length != 1) {
            System.out.println("You have not entered the port number correctly");
            System.out.println("[port]\n");
            return;
        }

        ServerSocket serverSocket;
        int port = -1;

        try {
            //parse the port number as int
            port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            System.out.println("Server could not be assigned to port " + port + "!");
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                new ServerSide(serverSocket.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


