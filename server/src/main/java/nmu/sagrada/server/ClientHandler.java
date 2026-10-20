package nmu.sagrada.server;

import nmu.sagrada.protocol.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

final class ClientHandler {
    private final Socket socket;
    private final GameCoordinator coordinator;
    private final BlockingQueue<Message> outgoing = new LinkedBlockingQueue<>();
    private final AtomicBoolean closed = new AtomicBoolean();
    private ObjectOutputStream output;
    private Thread writer;
    private String playerId;

    ClientHandler(Socket socket, GameCoordinator coordinator) {
        this.socket = socket;
        this.coordinator = coordinator;
    }

    void start() {
        Thread reader = new Thread(this::readLoop, "sagrada-client-reader");
        reader.start();
    }

    void send(Message message) {
        if (!closed.get()) outgoing.offer(message);
    }

    String getPlayerId() { return playerId; }
    void setPlayerId(String playerId) { this.playerId = playerId; }

    private void readLoop() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            writer = new Thread(this::writeLoop, "sagrada-client-writer");
            writer.start();
            while (!closed.get()) {
                Object value = input.readObject();
                if (!(value instanceof Message)) throw new IOException("Unsupported payload");
                coordinator.onMessage(this, (Message) value);
            }
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException error) {
            if (!closed.get()) System.err.println("Client connection failed: " + error.getMessage());
        } finally {
            close();
        }
    }

    private void writeLoop() {
        try {
            while (!closed.get()) {
                Message message = outgoing.take();
                output.writeObject(message);
                output.flush();
                output.reset();
            }
        } catch (IOException | InterruptedException error) {
            if (!closed.get()) System.err.println("Client writer failed: " + error.getMessage());
        } finally {
            close();
        }
    }

    void close() {
        if (!closed.compareAndSet(false, true)) return;
        coordinator.onDisconnect(this);
        if (writer != null) writer.interrupt();
        try { socket.close(); } catch (IOException ignored) {}
    }
}
