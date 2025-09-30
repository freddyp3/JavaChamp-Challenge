package persistence;

import model.Leaderboard;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriteLeaderboardTest extends JsonLeaderboardTest {

    @Test
    void testLeaderboardWriterInvalidFile() {
        try {
            Leaderboard leaderboard = new Leaderboard();
            JsonWriterLeaderboard writer = new JsonWriterLeaderboard("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testLeaderboardWriterEmpty() {
        try {
            Leaderboard leaderboard = new Leaderboard();
            JsonWriterLeaderboard writer = new JsonWriterLeaderboard("./data/testJson/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(leaderboard);
            writer.close();

            JsonReaderLeaderboard reader = new JsonReaderLeaderboard("./data/testJson/testWriterEmptyWorkroom.json");
            leaderboard = reader.read();
            assertEquals(0, leaderboard.getHighScore("EASY"));
            assertEquals(0, leaderboard.getHighScore("MEDIUM"));
            assertEquals(0, leaderboard.getHighScore("HARD"));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testLeaderboardWriterGeneral() {
        try {
            Leaderboard leaderboard = new Leaderboard(2, 4, 0);
            leaderboard.updateHighScore("HARD", 10);
            leaderboard.updateHighScore("EASY", 11);
            JsonWriterLeaderboard writer = new JsonWriterLeaderboard("./data/testJson/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(leaderboard);
            writer.close();

            JsonReaderLeaderboard reader = new JsonReaderLeaderboard("./data/testJson/testWriterGeneralWorkroom.json");
            leaderboard = reader.read();
            assertEquals(11, leaderboard.getHighScore("EASY"));
            assertEquals(4, leaderboard.getHighScore("MEDIUM"));
            assertEquals(10, leaderboard.getHighScore("HARD"));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}