package ScrabbleClient;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class ScrabbleClient {
    private Socket client = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    static boolean isConnected = false;


    public static void main(String[] args) {
        ScrabbleClient scrabbleClient = new ScrabbleClient();
        String host = "localhost";
        String port = "1234";
        scrabbleClient.clientConnect(host, port);
        scrabbleClient.start();

    }

    public void start() {
        if (beConnected()) {
            ClientGUI clientGUI = new ClientGUI(dis, dos, client);
        }
    }


    public void clientConnect(String host, String port) {
        try {
            client = new Socket(host, Integer.parseInt(port));//Connect with the Server
            isConnected = true;
            OutputStream sOut = client.getOutputStream();
            InputStream sIn = client.getInputStream();
            dos = new DataOutputStream(sOut);
            System.out.println("connected with sever");
            dis = new DataInputStream(sIn);
        } catch (UnknownHostException uhe) {
            System.out.println("Unknown host: " + host);
        } catch (IOException ioe) {
            System.out.println("Cannot connect the server");
            System.out.println("Closing");
            System.exit(0);
        }
    }

    public boolean beConnected() {
        return isConnected;
    }
}