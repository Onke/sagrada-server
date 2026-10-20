package nmu.sagrada.server;

import nmu.sagrada.protocol.Difficulty;
import nmu.sagrada.protocol.GameMode;
import nmu.sagrada.protocol.GameSnapshot;
import nmu.sagrada.protocol.Join;
import nmu.sagrada.protocol.Joined;
import nmu.sagrada.protocol.LobbyState;
import nmu.sagrada.protocol.Message;
import nmu.sagrada.protocol.Quit;
import nmu.sagrada.protocol.SetupState;
import nmu.sagrada.protocol.WindowSelected;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketIntegrationTest {
    @Test void oneHumanCanJoinBotMatchAndReachFirstTurnSnapshot() throws Exception {
        GameCoordinator coordinator = new GameCoordinator();
        try (ServerSocket listener = new ServerSocket(0);
             Socket client = new Socket("127.0.0.1", listener.getLocalPort());
             Socket accepted = listener.accept()) {
            client.setSoTimeout(5000);
            new ClientHandler(accepted, coordinator).start();

            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            output.writeObject(new Join("Test Player", GameMode.VS_BOTS, 1, Difficulty.MEDIUM));
            output.flush();

            Joined joined = assertInstanceOf(Joined.class, read(input));
            assertTrue(joined.host);
            LobbyState lobby = assertInstanceOf(LobbyState.class, read(input));
            assertEquals(2, lobby.players.size());
            SetupState setup = assertInstanceOf(SetupState.class, read(input));
            assertEquals(4, setup.windowChoices.size());

            output.writeObject(new WindowSelected(setup.windowChoices.get(0)));
            output.flush();
            GameSnapshot snapshot = assertInstanceOf(GameSnapshot.class, read(input));
            assertEquals(2, snapshot.players.size());
            assertEquals(5, snapshot.draftPool.size());

            output.writeObject(new Quit());
            output.flush();
        }
    }

    private Message read(ObjectInputStream input) throws Exception {
        return (Message) input.readObject();
    }
}
