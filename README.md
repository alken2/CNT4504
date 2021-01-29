# CNT4504

*Note: Our group contains the following members: Patrick Saloum, Javen Watson, Jon Mikael Transfiguracion, Kenneth Alcineus, and William Relken. The following README contains much of the contents of the group paper submitted for the course.*

# Network Management Application using the Sockets API (November 2020)

**Patrick S., Jon T., Kenneth A., Javen W., and William R., Student, University of North Florida**

## Abstract

The Client-Server projects contain 3 classes for the first project (ClientOptions, ClientSideNew, and ServerSideNew) and 4 classes for the second project (ClientSide, ClientThreader, ServerSide, ServerThreader). The first project has an iterative server test and the second project has a concurrent server test; both tests will measure the latency between the clients and server depending on the number of clients attempting to connect. The tests simulate multiple client connections at once and the response times for getting the output of server commands under light load and heavy load are measured.

## 1.0 Introduction

This project will measure the latency of the server depending on the number of clients as well as how intensive the load is on the server. The first test will use three classes(ClientOptions,ClientSideNew,ServerSideNew) to simulate an iterative approach; The ClientSideNew uses an array of type Thread to store ClientOptions that act as clients and then run them one at a time, the ServerSideNew will address the threads one at a time beginning with the first thread in the array. The remaining threads will wait for the current thread connecting to the ServerSideNew to die before running.

The next test will have four classes (ClientSide, ClientThreader,ServerSide,ServerThreader), this test will simulate a concurrent server. The ClientThreader will spawn threads that each will have ClientSide objects, who will attempt to connect to an ServerSide object. The ServerThreader will have a loop that will spawn ServerSide objects that extends Threads for each ServerSide object that it tries to connect to it.

> “The focus of the projects in this course is to be able to compare, in quantitative and qualitative terms, the two approaches commonly used in the development of distributed applications. Project 1 deals with the first approach, i.e., the socket-based approach. Project 2 deals with the RPC/RMI based approach. The paper that you will write at the end of these projects will compare these two approaches in quantitative and qualitative terms. You will be required to measure the mean response time using the socket and the RPC/RMI based approaches.”[1]
  

## 2.0 Distributed Applications Development

Both projects will have threaded clients to be able to simulate multiple clients for the server. The first project uses the iterative server, the second project uses the concurrent server. The iterative server processes each client one by one; it only handles one client at a time and the rest of the clients wait for their turn. The concurrent server uses threads to spawn servers so that each can handle a client; By doing so, the concurrent server can pass a client off to a server. The concurrent server can handle multiple client requests at the same time [1]. For both projects, a menu of options on the client side will be displayed to the console until the selected option is equal to ‘7’. Each option except for option ‘7’ will result in a response from the server. Option ‘1’ is to get the machine date and time. Option ‘2’ is to get the machine uptime. Option ‘3’ is to get the machine memory usage. Option ‘4’ is to get the machine network statistics. Option ‘5’ is to get the current users on the machine. Option ‘6’ is to get the running processes on the machine. Option ‘7’ is to quit the client-side program. The server side for both projects will run endlessly once started, and will respond appropriately to options sent by connected clients.


    Socket(arguments) - constructs sockets, used to create a socket in ServerThreader, ClientSide, and ClientThreader.
    .accept() - passively listens for and accepts connetions.
    .flush() – forces sockets to deliver payload.
    .close() – closes socket, it is no longer transmitting.

**Pseudocode for server requests**

    case 1 -> SendProcess("date", clientSocket)
    case 2 -> SendProcess("uptime", clientSocket)
    case 3 -> SendProcess("free", clientSocket)
    case 4 -> SendProcess("netstat", clientSocket)
    case 5 -> SendProcess("users", clientSocket)
    case 6 -> SendProcess("ps -aux", clientSocket)
    case 7 -> clientsocket.close()

### 2.1 Iterative Approach

The ClientSideNew class is for running the client side of the first project. To run the client, the command
line arguments for server hostname, server port, and number of threads must be entered in that order.
The program will return if the wrong amount of arguments is entered. Once the program is started, an
array of threads that has a size equal to the number of threads argument is instantiated. First, a menu of
options is displayed for the user to send requests to the server and receive the appropriate responses.
After that, a Scanner object is then instantiated to take the user input, and will keep taking user input if it
does not match the options available. Next, the number of threads and the selected menu option will be
written to a file called “responsetimes.txt”. Finally, a method is called for assigning threads to the thread
array, running each thread using the start method, and joining them and the process repeats.
Each element of the thread array has a new ClientOptions object assigned to it; this is possible because
ClientOptions extends to the Thread class.

The ClientOptions class constructs a ClientOptions object using the hostname, port, and selected menu
option passed from the ClientSideNew class. The run method for ClientOptions overrides the Thread run
method. A Socket object using the passed hostname and port is created and the variables to measure the
server latency are instantiated. A PrintWriter object is created to send the selected menu option to the
server and a BufferedReader object is created to receive the output depending on the menu option from
the server as long as the selected menu option is not ‘7’. For menu options that are not ‘7’, the timer is
started, the menu option is sent to the server, the response is received from the server, the timer is
stopped, and the result of the final time minus the initial time is written to a file. If the menu option is ‘7’,
the menu option is sent to the server and the PrintWriter, BufferedReader, and Socket objects are closed
without getting a response from the server.

The ServerSideNew class is for running the server side of the first project. To run the server, the command
line argument for port must be entered. A ServerSocket object is then instantiated and the program will
stop if the ServerSocket could not be assigned to the port. First, the server will wait for a client to connect
using the and assign a client to a Socket object using the ServerSocket accept method. Once a connection
is established, a BufferedReader object is created to get the selected menu option from the client. Next,
for any menu option that isn’t ‘7’, a method is called to execute the respective process on the machine
and then write the output to the client using a PrintWriter object. Finally, if the menu option is ‘7’, the
BufferedReader and Socket objects are closed. Given that this is an iterative server, the process will repeat
once at a time for each remaining client thread.

### 2.2 Concurrent Approach

With the exception of how response times are displayed and the addition of code that will prevent the client side from running if a connection to the server is not possible, the client side and the client thread implementation of the second project remains largely unchanged compared to those of the first project. In this project, ClientSideNew has been renamed to ClientThreader and ClientOptions has been renamed to ClientSide. However, the server side of the second project has undergone significant changes compared to the server side of the first project. ServerSideNew has also been renamed to ServerSideThreader and a new class called ServerSide is created to allow for concurrent server connections.
The ServerSideThreader class is for running the server side of the second project. The server side is ran using the same command line argument as the server side for the first project and will also check if a ServerSide object could be assigned to that port. Unlike the first project, there is an endless loop that will start server threads. A new ServerSide object, which extends to the Thread class, will be created and ran for each client accepted by the ServerSocket.

The ServerSide class constructs a ServerSide object using the Socket passed from the accept method of the ServerSocket in the ServerSideThreader class. The run method for ServerSide overrides the Thread run method. The ServerSide run method applies nearly the exact same functionality of the ServerSideNew class in the first project but uses the passed Socket object as the client instead.

## 3.0 Result and Comparisons

**Concurrent Test**
![Concurrent Test Results](/Images/concurrent.png)

*Results and comparisons of performing a Concurrent Test*

> The response time charts show a nonlinear but upwards trend in both comparisons meaning that the more clients request information, the more latency is experienced by the set up.

**Iterative Test**
![Iterative Test Results](/Images/iterative.png)

*Results and comparisons of performing an Iterative Test*

### 3.1 Test Bed

> The Client and Server were set up in two different VMs hosted on the same physical machine with the following resources: four gigabytes of ram, two logical processors, and running a Linux operating system. (cisvm-wkstn2-134 runs the server side, cisvm-wkstn2-104 runs the client)

**lshw -short**
![lshw -short output](/Images/lshw-short.png)
*Server Specs such as memory, processor and storage used in the tests.*

**hostnamectl**
![hostnamectl output](/Images/hostnamectl.png)
*Machine Hostnames and ID's.*

### 3.2 Studies carried out
In order to test out the relationship between latency and the number of clients, two different tests were carried out: light load and heavy load. The light load is the date and time call and the heavy load is the netstat call. Both server types were tested with the light load and heavy load, the number of clients were adjusted between one, five, ten, and then added increments of ten until one hundred. There are two graphs per test, there is the light load and heavy load for the iterative server and the light load and heavy load for the concurrent server.

### 3.3 Results

> Concurrent servers can spawn enough threads to deal with many requests that individually does not stress the server.

> The latency of the light load for the iterative server was not as smooth as the light load for the concurrent server.

> Meanwhile, latency for the iterative server was much smoother compared to that of the concurrent server; While extreme variances were noted in the response times of the concurrent servers, we consider this a problem of the server unable to handle all of the threads during the runtime.

## 4.0 Conclusion

The iterative server showed an exponential growth in response times as clients were added. This was due to the server having to process requests individually, leading to higher latency as the clients are increased. The concurrent server showed a slower growth in response time as clients were added. This is the result of the server being able to process requests in parallel by the threads created. The graphs of both, heavy and light load for the concurrent server, show a linear growth as more clients were added.

Possible causes for extraneous results may be the result of background processes or network interruptions. An example of such results can be seen in the “Iterative Server Results (Light Load)” graph. Once the number of clients reached 90 there was a large drop-in response time. These erroneous values could be prevented by using a larger sample size, thus accounting for performance increase or decrease. The response times for the concurrent server’s light load from 10-60 clients had remained nearly unchanged. The reasoning on this is because the server can only initiate a certain amount of threads before the thread count exceeds the handled server capacity. This trend was not observed in the concurrent server’s heavy load.

The concurrent server has more stable performance when performing multiple heavy loads, also it has less latency in the one to thirty client’s range. The concurrent server has better performance and stability when dealing with the light load. Both servers started struggling when the number of clients reached around forty, considering the test bed and hardware constraints, this is normal. At high user counts the presented results are not representative of a perfect concurrent server’s efficiency. This is caused by the physical restrictions of the CPU. When all available CPU threads are in use the other requests must wait for an active thread to end. Once the concurrent server reaches its maximum capacity it behaves like a pseudo-iterative server to clear out its buffer.

Based off the experimental results presented in this paper, a concurrent server is a more practical implementation of a server. It can handle a larger volume of requests at a time. The received load, both heavy and light, are independent of the growth of response times for the server.

## REFERENCES
[1] Ibm.com. 2020. IBM Knowledge Center. [online] Available at: <https://www.ibm.com/support/knowledgecenter/SSLTBW_2.1.0/com.ibm.zos.v2r1.hali001/concurrentanditerativeservers.htm> [Accessed 27 November 2020].

[2] Y.-C. Chen, Y.-s. Lim, R. J. Gibbens, E. M. Nahum, R. Khalili, and D. Towsley. A measurement-based study of MultiPath TCP performance over wireless networks. In Proceedings of the 2013 Conference on Internet Measurement Conference, IMC ’13, pages 455–468, New York, NY, USA, 2013. ACM.
