package nmu.sagrada;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    private int numPlayers = 0;

    public Server() throws IOException {
        server = new ServerSocket(5000);
        System.out.printf("Sagrada sever started on : %s: 5000", InetAddress.getLocalHost().getHostAddress());

        while (true) {
            Socket client = server.accept();
            System.out.printf("\nConnection request received: %s", client.getInetAddress().getHostAddress());
            numPlayers++;
            ClientHandler clientHandler = new ClientHandler(client, numPlayers);

        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
