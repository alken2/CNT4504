import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.lang.Boolean;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//>set CLASSPATH=%CLASSPATH%;.
public class ClientSideNew extends Thread{
    public static void main(String[] args) {
        //test if there are too few arguments
        //args[0] = IP args[1] == port
        if (args.length != 3) {
            System.out.println("You have entered the wrong number of arguments");
            System.out.println("[hostname] [port] [threads]\n");
            return;
        }
        //Socket(String hostname, int port)
        //cast the hostname
        String hostname = args[0];
        //parse the port number as int
        int port = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);
        Thread[] threadList = new Thread[threadCount];
        CreateFile responseTimeFile = new CreateFile();
        char menuSelected;


        do{
            System.out.println("What input do you want " + threadCount + " Client(s) to send: ");
            menuSelected = GetUserInput();
            WriteToFile(threadCount, menuSelected);
            runThreads(hostname, port, menuSelected, threadList);
        } while(menuSelected != '7');

    }

    public static void runThreads(String hostname, int portNumber, char menuSelected, Thread[] threadList){
        for (int i = 0; i < threadList.length; i++) {
            threadList[i] = new ClientOptions(hostname, portNumber, menuSelected);
        }

        for (int i = 0; i < threadList.length; i++) {
            threadList[i].start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static void displayMenu(){
        System.out.println("_______________________________");
        System.out.println(" 1. Host current Date and Time");
        System.out.println(" 2. Host uptime");
        System.out.println(" 3. Host memory use");
        System.out.println(" 4. Host Netstat");
        System.out.println(" 5. Host current users");
        System.out.println(" 6. Host running processes");
        System.out.println(" 7. Quit");
        System.out.println("_______________________________");
    }

    static char GetUserInput() {
        char menuSelected;

        //show the options
        displayMenu();

        //create scanner object to read what the client enters
        Scanner scanner = new Scanner(System.in);

        //prompt input for response
        System.out.print("\nPlease choose an option: ");
        String validate = scanner.nextLine();

        while (validate.length() != 1) {
            System.out.print("\nYou have entered an invalid option. Please try again: ");
            validate = scanner.nextLine();
        }

        menuSelected = validate.charAt(0);

        return menuSelected;
    }

    public static void WriteToFile(int threadCount, char OptionEntered){
        try{

            BufferedWriter writer = new BufferedWriter(new FileWriter("responseTimes.txt", true));
            writer.write("Threads Made: " + (int)threadCount);
            writer.newLine();
            writer.write("Menu Option Selected: " + OptionEntered);
            writer.newLine();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

class CreateFile{
    public static void main(String[] args){
        try {
            File responseTimes = new File("responsetimes.txt");
            System.out.println("Attempting to create file: " + responseTimes.getName());
            if (responseTimes.createNewFile() == false){
                System.out.println("File " + responseTimes.getName() + " already exists.");
                System.out.println("Response times being appended to : " + responseTimes.getName());
            }
            else{
                System.out.println("Response times being written to : " + responseTimes.getName());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}