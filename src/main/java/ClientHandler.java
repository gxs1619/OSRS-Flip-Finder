import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;

/**
 * Written by: Griffin Seibold
 * 1/30/2019
 * This class handles interactions with each client on a separate thread so that
 * the server can keep allowing new clients to connect and multiple requests can
 * be handled at once.
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private RunescapeAPICommunicator rsComm;
    private int numberOfRequests = 0;
    private SQLPopulater sql;
    private String response;
    private double requestCount = 0;

    /**
     * Constructor for a Client Handler object.
     *
     * @param clientSocket The socket the client is on.
     * @param rsComm       the Object that communicates with the API and can get info on items.
     */
    public ClientHandler(Socket clientSocket, RunescapeAPICommunicator rsComm, String response, SQLPopulater sql) {
        this.clientSocket = clientSocket;
        this.rsComm = rsComm;
        this.sql = sql;
        this.response = response;
    }

    /**
     * Communicates with the client, records their request, sends the request off
     * to be processed and then sends the processed response back to the client.
     */
    @Override
    public void run() {
        BufferedReader input;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            while (clientSocket.isConnected()) {
                String request = input.readLine();
                if(request == null){
                    System.out.println("Client Disconnected at " + clientSocket.getInetAddress().toString());
                    return;
                }
                else{
                    System.out.println(request);
                    String response = multiRequests(request);
                    out.println(response);
                }
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Client not found");
        }
    }

    /**
     * First method that deals with handling requests, this one is for
     * dealing with multiple clients trying to make requests at the same time.
     */
    private synchronized String multiRequests(String request) {
        while (numberOfRequests > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        numberOfRequests++;
        String response = processRequest(request);
        if (numberOfRequests > 0) {
            numberOfRequests--;
            notifyAll();
        }

        return response;
    }

    /**
     * Second method that deals with handling requests, this one deals with the
     * parsing of the client's message. Based on the request type, this will
     * determine what response is sent back to the client.
     */
    private synchronized String processRequest(String request) {
        requestCount++;
        String[] requestSplit = request.split(",");

        try {
            if (requestCount > 10) {
                rsComm.populateAll(sql);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (requestSplit[0].equals("connect")) {
            return "connected";
        }
        if (requestSplit[0].equals("update")) {
            rsComm.ItemBuilder(Integer.parseInt(requestSplit[1]), sql);
            return "done";
        }
        return requestSplit[0] + ",error,unknown request";
    }
}
