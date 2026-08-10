package nmu.sagrada.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public final class Server {
    public static final int PORT = 5000;

    private Server() {}

    public static void main(String[] args) throws IOException {
        GameCoordinator coordinator = new GameCoordinator();
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.printf("Sagrada server started at %s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), PORT);
            while (true) {
                Socket socket = server.accept();
                System.out.printf("Connection from %s%n", socket.getInetAddress().getHostAddress());
                new ClientHandler(socket, coordinator).start();
            }
        }
    }
}
