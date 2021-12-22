import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class GameFileIOTest {

    public static void main(String[] args) throws XMLStreamException, IOException {
        shouldSaveAndLoadSameGame();
        shouldSaveAndLoadSameGameWithAI();
    }

    private static void shouldSaveAndLoadSameGameWithAI() throws XMLStreamException, IOException {
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
        save = GameFileIO.loadGame("game_save_test.xml");
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
        save.setGameMode(Constants.MAN_VS_MAN_MODE);
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
