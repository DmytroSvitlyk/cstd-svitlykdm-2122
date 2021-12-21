import jssc.SerialPortException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class GameTest {

    private static Game game;

    public static void main(String[] args){
        try {
            game = new Game(args[0]);
            startingGameShouldOpenPort();
            playerShouldMakeMove();
            playerShouldNotMakeMoveIfCellIsOccupied();
            closingGameShouldClosePort();
        } catch (SerialPortException e) {
            System.out.println(e.getPortName() + " " + e.getMethodName() + " " + e.getExceptionType());
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void startingGameShouldOpenPort() throws SerialPortException, XMLStreamException, IOException {
        game.start();
        assert game.getConnector().portIsOpened();
    }

    private static void closingGameShouldClosePort() throws SerialPortException {
        game.close();
        assert !game.getConnector().portIsOpened();
    }

    private static void playerShouldMakeMove() throws SerialPortException {
        game.makeMove((byte) 0, (byte) 1, Constants.PLAYER_X);
        assert game.getConnector().getCell((byte) 0, (byte) 1) == Constants.PLAYER_X;
    }

    private static void playerShouldNotMakeMoveIfCellIsOccupied() throws SerialPortException {
        assert !game.makeMove((byte) 0, (byte) 1, Constants.PLAYER_O);
    }

    private static void playerShouldWinIfThreeInRow() throws SerialPortException {
        game.makeMove((byte) 0, (byte) 0, Constants.PLAYER_X);
        game.makeMove((byte) 0, (byte) 2, Constants.PLAYER_X);
        assert game.getConnector().checkForWin() == Constants.PLAYER_X;
    }

}
