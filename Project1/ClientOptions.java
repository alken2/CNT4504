import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ClientOptions extends Thread implements Runnable {
    String hostname;
    char menuSelected;
    int portNumber;

    public ClientOptions(String hostname, int portNumber, char menuSelected) {
        this.hostname = hostname;
        this.menuSelected = menuSelected;
        this.portNumber = portNumber;
    }

    public void run() {
        long startCountMillis;
        long endCountMillis;
        //variable to hold response from server
        String temp;

        try (Socket socket = new Socket(hostname, portNumber)) {

            //create input and output stream for the socket
            PrintWriter writeToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));


            //send the response to server aka write it to the Writer depending on the option selected
            if (menuSelected != '7') {
                String output = "";

                startCountMillis = System.currentTimeMillis();

                writeToServer.println(menuSelected + "");
                writeToServer.flush();

                while ((temp = readFromServer.readLine()) != null) {
                    System.out.println(temp);
                }

                endCountMillis = System.currentTimeMillis();

                endCountMillis = endCountMillis - startCountMillis;
                WriteToFile(endCountMillis);

            } else {
                System.out.println("Ending...\n");
                writeToServer.println("7");
                writeToServer.flush();
                writeToServer.close();
                readFromServer.close();
                socket.close();
            }

        }
        //catch host not found and I/O error
        catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    public static void WriteToFile(long responseTime){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("responseTimes.txt", true));
            writer.write("Response time: " + (int)responseTime);
            writer.newLine();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

