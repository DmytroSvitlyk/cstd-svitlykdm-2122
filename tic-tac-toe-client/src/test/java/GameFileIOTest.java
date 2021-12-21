import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class GameFileIOTest {

    public static void main(String[] args) throws XMLStreamException, IOException {
        shouldSaveAndLoadSameGame();
    }

    private static void shouldSaveAndLoadSameGame() throws XMLStreamException, IOException {
        byte[][] f = new byte[][]{
                {Constants.PLAYER_X, Constants.PLAYER_O, Constants.PLAYER_X},
                {Constants.PLAYER_X, Constants.EMPTY, Constants.PLAYER_X},
                {Constants.PLAYER_X, Constants.PLAYER_O, Constants.EMPTY }
        };
        byte currentPlayer = Constants.PLAYER_X;
        GameSave save = new GameSave();
        save.setCurrentPlayer(currentPlayer);
        save.setField(f);
        GameFileIO.saveGame(save, "game_save_test.xml");
        save = GameFileIO.loadGame("game_save_test.xml");
        assert currentPlayer == save.getCurrentPlayer();
        for (int i = 0; i < Constants.FIELD_SIZE; i++) {
            for (int j = 0; j < Constants.FIELD_SIZE; j++) {
                assert f[i][j] == save.getField()[i][j];
            }
        }
    }

}
