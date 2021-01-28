import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

//>set CLASSPATH=%CLASSPATH%;.
        public class ClientSideThreader extends Thread{
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


                char menuSelected;

                try {
                    Socket checkServer = new Socket(hostname, port);
                    checkServer.close();
                }
                catch (IOException e) {
                    System.out.println("Could not resolve hostname and/or port!");
                    System.out.println("[hostname] [port] [threads]\n");
                    return;
                }

                do{
                    System.out.println("What input do you want " + threadCount + " Client(s) to send: ");
                    //attempts to create a file responseTimes.txt
                    //actual response data is recorded by each thread of ClientOptions
                    CreateFile();
                    menuSelected = GetUserInput();
                    WriteToFile(threadCount, menuSelected);
                    runThreads(hostname, port, menuSelected, threadList);
                    if(menuSelected != '7'){
                        ReadFromFile();
                    }
                } while(menuSelected != '7');

            }
            //creates a clientOptions object for each thread in threadList
            //initiates those threads and finally waits for the threads to die
            public static void runThreads(String hostname, int portNumber, char menuSelected, Thread[] threadList){
                for (int i = 0; i < threadList.length; i++) {
                    //each thread in the list is a new object of ClientOptions
                    threadList[i] = new ClientSide(hostname, portNumber, menuSelected);
                }

                //.start() is used instead of .run() as it calls .run() asynchronously
                for (Thread value : threadList) {
                    value.start();
                }

                //using .join makes the program wait for each thread to die
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

                while (validate.length() != 1 || (Integer.parseInt(validate) < 1 || Integer.parseInt(validate) > 7)) {
                    System.out.print("\nYou have entered an invalid option. Please try again: ");
                    validate = scanner.nextLine();
                }

                menuSelected = validate.charAt(0);

                return menuSelected;
            }

            public static void CreateFile(){
                try {
                    File responseTimes = new File("responseTimes.txt");
                    if (responseTimes.delete()) {
                        System.out.println("Deleting old file: " + responseTimes.getName());
                    }
                    if (responseTimes.createNewFile()) {
                        System.out.println("Creating new file: " + responseTimes.getName());
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            public static void WriteToFile(int threadCount, char OptionEntered){
                try{

                    BufferedWriter writer = new BufferedWriter(new FileWriter("responseTimes.txt", true));
                    writer.write("Threads Made: " + threadCount);
                    writer.newLine();
                    writer.write("Menu Option Selected: " + OptionEntered);
                    writer.newLine();
                    writer.write("Response Times: ");
                    writer.newLine();
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            //this method will read the data from the file responseTimes.txt into an array
            //the array will then be parsed to get data from strings and finally print
            public static void ReadFromFile(){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("responseTimes.txt"));

                    ArrayList<String> stringData = new ArrayList<>();
                    ArrayList<Integer> intData = new ArrayList<>();
                    String temp;

                    while((temp = reader.readLine()) != null){
                        stringData.add(temp);
                    }
                    for(int i = 3; i < stringData.size();i++){
                        intData.add(Integer.valueOf(stringData.get(i)));
                    }

                    float responseAverage = intData.get(0);
                    for(int i = 0; i < intData.size(); i++){
                        if((i + 1) < intData.size()){
                            responseAverage = responseAverage + intData.get(i+1);
                        }
                    }
                    responseAverage = responseAverage / intData.size();

                    for(int x : intData){
                        System.out.println("Response Time: " + x);
                    }
                    System.out.println("Average Response Time: " + responseAverage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

