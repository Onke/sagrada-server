package nmu.sagrada;

import nmu.sagrada.messages.Message;
import nmu.sagrada.messages.client.Quit;
import nmu.sagrada.messages.server.LobbyPlayersListed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientHandler {

    private Socket client;
    private int numPlayers;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private BlockingQueue<Message> outgoingMessages = new LinkedBlockingDeque<>();
    private ReadThread readThread;
    private WriteThread writeThread;


    public ClientHandler(Socket client, int numPlayers) {
        this.client = client;
        this.numPlayers = numPlayers;

        readThread = new ReadThread();
        readThread.start();

    }

    public void send(Message message) {
        try {
            outgoingMessages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("\n" + numPlayers + ": Read thread started...");

                in = new ObjectInputStream(client.getInputStream());
                out = new ObjectOutputStream(client.getOutputStream());
                out.flush();


                System.out.println(numPlayers + ": Obtained I/O streams");

                writeThread = new WriteThread();
                writeThread.start();

                System.out.println(numPlayers + ": Started read loop...");
                Message message;
                do {
                    message = (Message) in.readObject();
                    System.out.println(numPlayers + " >>> " + message);

                    message.apply(ClientHandler.this);
                } while (message.getClass() != Quit.class);

                client.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                System.out.println(numPlayers + ": disconnecting...");
                writeThread.interrupt();
            }
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {
            System.out.println("Starting write loop thread...");
            writeThread = this;
            try {
                while (!isInterrupted()) {
                    Message message = outgoingMessages.take();

                    out.writeObject(message);
                    out.flush();

                    System.out.println(numPlayers + " <<< " + message);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeThread = null;
                System.out.println(numPlayers + ": Write thread finished.");
            }
        }
    }
}
