import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerSide extends Thread{
    private final Socket passedClientSocket;
    public ServerSide(Socket socket){
        passedClientSocket = socket;
    }

    public void run() {
        try {
            //server hears client socket and accepts connection with serverSock
            System.out.println("Awaiting a new connection...");
            Socket clientSocket = passedClientSocket;
            System.out.println("Connection established");
            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            String selection;



            //string to read selection from client
            selection = readFromClient.readLine();

            switch (selection) {
                case ("1"):
                    System.out.println("Sending current date and time...");
                    SendProcess("date", clientSocket);
                    break;
                case ("2"):
                    System.out.println("Sending current uptime...");
                    SendProcess("uptime -p", clientSocket);
                    break;
                case ("3"):
                    System.out.println("Sending current memory usage...");
                    SendProcess("free -b", clientSocket);
                    break;
                case ("4"):
                    System.out.println("Sending current Netstat...");
                    SendProcess("netstat", clientSocket);
                    break;
                case ("5"):
                    System.out.println("Sending current users...");
                    SendProcess("users", clientSocket);
                    break;
                case ("6"):
                    System.out.println("Sending current running processes...");
                    SendProcess("ps -aux", clientSocket);
                    break;
                case ("7"):
                    System.out.println("Ending connection...");
                    readFromClient.close();
                    clientSocket.close();
                    System.out.println("Connection ended");
                    break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("\nThe connection has been ended");
            e.printStackTrace();
        }
    }

    static void SendProcess(String processTerm, Socket clientSocket) throws IOException {
        PrintWriter writeToClient = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        Process process;
        BufferedReader inputFromProcess;
        String response;

        try {
            process = Runtime.getRuntime().exec(processTerm);
            inputFromProcess = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((response = inputFromProcess.readLine()) != null){
                writeToClient.println(response);
            }

            inputFromProcess.close();
            writeToClient.flush();
            clientSocket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}