import jssc.SerialPortException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Scanner;

public class Game implements AutoCloseable{

    private static boolean QUIT = false;
    private static boolean LOAD = false;
    private static byte MENU_CODE;
    final Scanner sc = new Scanner(System.in);
    final ServerConnector S;
    GameSave save = new GameSave();

    public Game(String comm) throws SerialPortException {
        this.S = new ServerConnector(comm);
    }

    public void start() throws SerialPortException, XMLStreamException, IOException {
        while(!QUIT) {
            save.reset();
            LOAD = false;
            System.out.println("WELCOME TO TIC-TAC-TOE");
            System.out.println("CHOOSE AN OPTION");
            System.out.println("1) Start New Game");
            System.out.println("2) Load Game");
            System.out.println("3) Quit");
            System.out.println("Enter option code: ");
            MENU_CODE = sc.nextByte();
            switch (MENU_CODE) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    loadGame();
                    break;
                case 3:
                    QUIT = true;
            }
        }
    }

    public void startNewGame() throws SerialPortException, XMLStreamException, IOException {
        System.out.println("CHOOSE GAME MODE");
        System.out.println("1) MAN vs MAN");
        System.out.println("2) MAN vs AI (random)");
        System.out.println("3) MAN vs AI (win strategy)");
        System.out.println("4) QUIT");
        MENU_CODE = sc.nextByte();
        switch (MENU_CODE) {
            case 1:
                startManVSManGame();
                break;
            case 2:
                startManVSAIGame(Constants.AI_RANDOM_MODE);
                break;
            case 3:
                startManVSAIGame(Constants.AI_WIN_MODE);
            case 4:
                break;
        }

    }

    public void loadGame() throws XMLStreamException, IOException, SerialPortException {
        LOAD = true;
        save = GameFileIO.loadGame(Constants.SAVE_FILE);
        if(save.isInvalidSave()) {
            System.out.println("Some options in save is unset, Save is corrupted");
        }
        switch (save.getGameMode()) {
            case Constants.MAN_VS_MAN_MODE:
                startManVSManGame();
                break;
            case Constants.MAN_VS_AI_MODE:
                startManVSAIGame(save.getAiMode());
                break;
            default:
                System.out.println("UNRECOGNIZED GAME MODE");
                break;
        }
    }

    public void startManVSAIGame(byte aiMode) throws SerialPortException, XMLStreamException, IOException {
        QUIT = false;
        byte player, ai, currentPlayer = Constants.PLAYER_X, winner, x, y;
        if(LOAD) {
            player = save.getPlayerSymbol();
            ai = (player == Constants.PLAYER_X ? Constants.PLAYER_O : Constants.PLAYER_X);
            currentPlayer = save.getCurrentPlayer();
            S.loadField(save.getField());
        } else {
            System.out.println("CHOOSE A SYMBOL (X - 1 / O - 2): ");
            int curr = sc.nextInt();
            System.out.println("YOU NOW PLAYING AS: " + GameFileIO.byteToStringPlayer((byte)curr));
            player = (byte) curr;
            ai = (player == Constants.PLAYER_X ? Constants.PLAYER_O : Constants.PLAYER_X);
        }
        while (!QUIT) {
            printField();
            winner = S.checkForWin();
            if(winner == Constants.DRAW) {
                System.out.println("OOPS, THERE IS A DRAW");
                return;
            }
            if(winner != Constants.EMPTY) {
                System.out.println("HERE IS THE WINNER");
                System.out.println("PLAYER " + (winner == Constants.PLAYER_X ? "X" : "O") +  " WINS");
                System.out.println();
                return;
            }
            if(currentPlayer == ai) {
                System.out.println("AI MOVES");
                if (aiMode == Constants.AI_RANDOM_MODE) {
                    S.makeRandomMove(ai);
                }
                if (aiMode == Constants.AI_WIN_MODE) {
                    S.makeBestMove(ai);
                }
                currentPlayer = player;
            }
            else {
                System.out.println("PLAYER " + (currentPlayer == Constants.PLAYER_X ? "X" : "O") + " MOVES");
                System.out.println("CHOOSE AN OPTION");
                System.out.println("1) Make move");
                System.out.println("2) Save & Quit");
                System.out.println("3) Quit");
                MENU_CODE = sc.nextByte();
                switch (MENU_CODE) {
                    case 1:
                        System.out.println("Enter row and column separated by space: ");
                        x = sc.nextByte();
                        y = sc.nextByte();
                        if (!makeMove(x, y, currentPlayer)) {
                            System.out.println("Move does not saved, enter correct data");
                        } else {
                            currentPlayer = ai;
                        }
                        break;
                    case 2:
                        System.out.println("SAVING GAME");
                        save.setCurrentPlayer(currentPlayer);
                        save.setField(S.saveField());
                        save.setGameMode(Constants.MAN_VS_AI_MODE);
                        save.setAiMode(aiMode);
                        save.setPlayerSymbol(player);
                        GameFileIO.saveGame(save, Constants.SAVE_FILE);
                        System.out.println("GAME SAVED. EXITING...");
                        return;
                    case 3:
                        System.out.println("EXIT.");
                        return;
                }
            }
        }
    }

    public void startManVSManGame() throws SerialPortException, XMLStreamException, IOException {
        byte currentPlayer;
        S.initField();
        QUIT = false;
        byte winner, x, y;
        if(LOAD) {
            S.loadField(save.getField());
            currentPlayer = save.getCurrentPlayer();
            System.out.println("GAME MAN VS MAN LOADED: ");
        } else {
            currentPlayer = Constants.PLAYER_X;
            System.out.println("GAME IN MAN VS MAN MODE STARTED: ");
        }
        while (!QUIT) {
            printField();
            winner = S.checkForWin();
            if(winner == Constants.DRAW) {
                System.out.println("OOPS, THERE IS A DRAW");
                return;
            }
            if(winner != Constants.EMPTY) {
                System.out.println("HERE IS THE WINNER");
                System.out.println("PLAYER " + (winner == Constants.PLAYER_X ? "X" : "O") +  " WINS");
                System.out.println();
                return;
            }
            System.out.println("PLAYER " + (currentPlayer == Constants.PLAYER_X ? "X" : "O") + " MOVES");
            System.out.println("CHOOSE AN OPTION");
            System.out.println("1) Make move");
            System.out.println("2) Save & Quit");
            System.out.println("2) Quit");
            MENU_CODE = sc.nextByte();
            switch (MENU_CODE) {
                case 1:
                    System.out.println("Enter row and column separated by space: ");
                    x = sc.nextByte();
                    y = sc.nextByte();
                    if(!makeMove(x, y, currentPlayer)) {
                        System.out.println("Move does not saved, enter correct data");
                    }
                    else {
                        currentPlayer = currentPlayer == Constants.PLAYER_X ? Constants.PLAYER_O : Constants.PLAYER_X;
                    }
                    break;
                case 2:
                    System.out.println("SAVING GAME");
                    save.setCurrentPlayer(currentPlayer);
                    save.setField(S.saveField());
                    save.setGameMode(Constants.MAN_VS_MAN_MODE);
                    GameFileIO.saveGame(save, Constants.SAVE_FILE);
                    System.out.println("GAME SAVED. EXITING...");
                    return;
                case 3:
                    System.out.println("EXIT.");
                    return;
            }
        }
    }

    public boolean makeMove(byte x, byte y, byte player) throws SerialPortException {
        if((x < 0 || x >= Constants.FIELD_SIZE) || (y < 0 || y >= Constants.FIELD_SIZE)) {
            return false;
        }
        byte cell = S.getCell(x, y);
        if(cell != Constants.EMPTY) {
            return false;
        }
        S.setCell(x, y, player);
        return true;
    }

    public void printField() throws SerialPortException {
        for (byte i = 0; i < Constants.FIELD_SIZE; i++) {
            for (byte j = 0; j < Constants.FIELD_SIZE; j++) {
                System.out.println(GameFileIO.byteToStringPlayer(S.getCell(i, j)) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public ServerConnector getConnector() {
        return S;
    }

    @Override
    public void close() throws SerialPortException {
        S.closeConnector();
        QUIT = true;
    }


}
