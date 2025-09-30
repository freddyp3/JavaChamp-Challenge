package persistence;

import model.Leaderboard;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderLeaderboardTest extends JsonLeaderboardTest {

    private JsonReaderLeaderboard reader;

    @Test
    void testReaderNonExistentFile() {
        reader = new JsonReaderLeaderboard("./data/testJson/nonexistent.json");
        try {
            Leaderboard leaderboard = reader.read();
            fail("IOException was expected but not thrown");
        } catch (IOException e) {
            // Test passes (IOException was expected)
        }
    }

    @Test
    void testReaderLeaderboard() {
        reader = new JsonReaderLeaderboard("./data/testJson/leaderboardEmpty.json");
        try {
            Leaderboard leaderboard = reader.read();
            assertNotNull(leaderboard);
            checkLeaderboard(0, 0, 0, leaderboard);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderGeneralLeaderboard() {
        reader = new JsonReaderLeaderboard("./data/testJson/leaderboardGeneral.json");
        try {
            Leaderboard leaderboard = reader.read();
            assertNotNull(leaderboard);
            checkLeaderboard(15, 12, 3, leaderboard);
        } catch (IOException e) {
            fail();
        }
    }
}