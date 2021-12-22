import java.util.Arrays;

public class GameSave {

    private byte currentPlayer;
    private byte[][] field = new byte[Constants.FIELD_SIZE][Constants.FIELD_SIZE];
    private byte gameMode;
    private byte aiMode;
    private byte playerSymbol;

    public byte getPlayerSymbol() {
        return playerSymbol;
    }

    public void setPlayerSymbol(byte playerSymbol) {
        this.playerSymbol = playerSymbol;
    }

    public byte getAiMode() {
        return aiMode;
    }

    public void setAiMode(byte aiMode) {
        this.aiMode = aiMode;
    }

    public byte getGameMode() {
        return gameMode;
    }

    public void setGameMode(byte gameMode) {
        this.gameMode = gameMode;
    }


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

    public boolean isInvalidSave() {
        if(currentPlayer == -1 || gameMode == -1)
            return false;
        return (gameMode != Constants.MAN_VS_AI_MODE) || ((aiMode != -1) && (playerSymbol != -1));
    }

    public void reset() {
        currentPlayer = -1;
        gameMode = -1;
        aiMode = -1;
        playerSymbol = -1;
    }
}
