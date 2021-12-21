import java.util.Arrays;

public class GameSave {

    private byte currentPlayer;
    private byte[][] field = new byte[Constants.FIELD_SIZE][Constants.FIELD_SIZE];

    public byte getGameMode() {
        return gameMode;
    }

    public void setGameMode(byte gameMode) {
        this.gameMode = gameMode;
    }

    private byte gameMode;

    public byte[][] getField() {
        return field;
    }

    public void setField(byte[][] field) {
        this.field = field;
    }

    public byte getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(byte currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "Current PLayer: " + currentPlayer + System.lineSeparator() +
                "Current Mode: " + gameMode + System.lineSeparator() +
                Arrays.toString(field[0]) +
                Arrays.toString(field[1]) +
                Arrays.toString(field[2]);
    }
}
