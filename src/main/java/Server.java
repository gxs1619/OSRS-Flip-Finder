import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;

/**
 * Written by: Griffin Seibold
 * 1/30/2019
 * This class creates a multi threaded server to interact with the database
 * and clients viewing the database.
 */
public class Server {
    private static LinkedList<ClientHandler> threads = new LinkedList<>();
    public final static String baseURL = "https://rsbuddy.com/exchange/summary.json";

    /**
     * Runnable method that creates a server and allows clients to connect.
     */
    public static void main(String[] args) throws NullPointerException{
        URL runescapeAPI;
        SQLPopulater sql;
        RunescapeAPICommunicator rsComm = new RunescapeAPICommunicator();
        try {        // Create a URL and open a connection
            String url = baseURL;
            runescapeAPI = new URL(url);
            sql = new SQLPopulater();
            ServerSocket serverSocket;
            rsComm.populateAll(sql);
            int port = 5151;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException("cannot open port " + port);
            }
            boolean isStopped = false;
            rsComm = new RunescapeAPICommunicator();
            while(!isStopped){
                Socket clientSocket;
                try{
                    clientSocket = serverSocket.accept();
                    System.out.println("Client accepted " + clientSocket.getInetAddress().toString());
                } catch (IOException e) {
                    if(isStopped){
                        System.out.println("Server Stopped.");
                        return;
                    }
                    throw new RuntimeException("Error accepting client connection", e);
                }
                threads.add(new ClientHandler(clientSocket,rsComm,"Connected.", sql));
                threads.getLast().start();
            }
            System.out.println("Server Stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
