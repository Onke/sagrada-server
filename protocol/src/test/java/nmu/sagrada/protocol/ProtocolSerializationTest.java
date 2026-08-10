package nmu.sagrada.protocol;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ProtocolSerializationTest {
    @Test void gameSnapshotRoundTripsWithoutFieldDrift() throws Exception {
        PlayerSnapshot player = new PlayerSnapshot("p1", "Player", false, "WINDOW1",
                Arrays.asList("RED4", "NULL"));
        GameSnapshot original = new GameSnapshot(3, "p1", Arrays.asList("BLUE2"),
                Arrays.asList(player), 42L);
        Message decoded = roundTrip(original);
        GameSnapshot snapshot = assertInstanceOf(GameSnapshot.class, decoded);
        assertEquals(42L, snapshot.revision);
        assertEquals("RED4", snapshot.players.get(0).dicePositions.get(0));
    }

    @Test void joinAndEndGameRoundTrip() throws Exception {
        Join join = assertInstanceOf(Join.class,
                roundTrip(new Join("Will", GameMode.VS_BOTS, 3, Difficulty.HARD)));
        assertEquals(3, join.botCount);
        assertEquals(Difficulty.HARD, join.difficulty);
        PlayerScore score = new PlayerScore("p1", "Will", false, 20, 8, 15, 3, "GREEN");
        EndGame end = assertInstanceOf(EndGame.class, roundTrip(new EndGame(Arrays.asList(score))));
        assertEquals(20, end.standings.get(0).total);
        assertEquals("GREEN", end.standings.get(0).privateObjective);
    }

    private Message roundTrip(Message message) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new ObjectOutputStream(bytes).writeObject(message);
        return (Message) new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();
    }
}
