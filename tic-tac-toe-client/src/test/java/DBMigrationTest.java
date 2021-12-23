import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;

public class DBMigrationTest {

    public static void main(String[] args) {
        try(DBConnector connector = new DBConnector()) {
            shouldWriteSaveFromFileAndReadSame(connector);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private static void shouldWriteSaveFromFileAndReadSame(DBConnector connector) throws XMLStreamException, IOException, SQLException {
        byte[][] f = new byte[][]{
                {Constants.PLAYER_X, Constants.PLAYER_O, Constants.PLAYER_X},
                {Constants.PLAYER_X, Constants.EMPTY, Constants.PLAYER_X},
                {Constants.PLAYER_X, Constants.PLAYER_O, Constants.EMPTY }
        };
        byte currentPlayer = Constants.PLAYER_X;
        GameSave save = new GameSave();
        save.setCurrentPlayer(currentPlayer);
        save.setField(f);
        save.setGameMode(Constants.MAN_VS_AI_MODE);
        save.setAiMode(Constants.AI_RANDOM_MODE);
        save.setPlayerSymbol(Constants.PLAYER_X);
        GameFileIO.saveGame(save, "game_save_test.xml");
        DBMigrationFromFile.migrateFromFile(Constants.SAVE_FILE);
        save = connector.readGameSave();
        assert currentPlayer == save.getCurrentPlayer();
        assert Constants.MAN_VS_AI_MODE == save.getGameMode();
        assert Constants.AI_RANDOM_MODE == save.getAiMode();
        assert Constants.PLAYER_X == save.getPlayerSymbol();
        for (int i = 0; i < Constants.FIELD_SIZE; i++) {
            for (int j = 0; j < Constants.FIELD_SIZE; j++) {
                assert f[i][j] == save.getField()[i][j];
            }
        }
    }

}
