import jssc.SerialPortException;

import java.util.Scanner;

public class Game implements AutoCloseable{

    private static boolean QUIT;
    private static byte MENU_CODE;
    final Scanner sc = new Scanner(System.in);
    final ServerConnector S;

    public Game(String comm) throws SerialPortException {
        this.S = new ServerConnector(comm);
    }

    public void start() throws SerialPortException {
        while(!QUIT) {
            System.out.println("WELCOME TO TIC-TAC-TOE");
            System.out.println("CHOOSE AN OPTION");
            System.out.println("1) Start Game");
            System.out.println("2) Quit");
            System.out.println("Enter option code: ");
            MENU_CODE = sc.nextByte();
            switch (MENU_CODE) {
                case 1:
                    startGame();
                    break;
                case 2:
                    QUIT = true;
            }
        }
    }

    public void startGame() throws SerialPortException {
        System.out.println("CHOOSE GAME MODE");
        System.out.println("1) MAN vs MAN");
        System.out.println("2) QUIT");
        // MORE MODES WILL BE ADDED LATER
        MENU_CODE = sc.nextByte();
        switch (MENU_CODE) {
            case 1:
                startManVSManGame();
                break;
            case 2:
                break;
        }

    }

    public void startManVSManGame() throws SerialPortException {
        S.initField();
        System.out.println("GAME IN MAN VS MAN MODE STARTED: ");
        QUIT = false;
        byte currentPlayer = Constants.PLAYER_X;
        byte winner, x, y;
        while (!QUIT) {
            printField();
            winner = S.checkForWin();
            if(winner != Constants.EMPTY) {
                System.out.println("HERE IS THE WINNER");
                System.out.println("PLAYER " + (winner == Constants.PLAYER_X ? "X" : "O") +  " WINS");
                System.out.println();
                return;
            }
            System.out.println("PLAYER " + (currentPlayer == Constants.PLAYER_X ? "X" : "O") + " MOVES");
            System.out.println("CHOOSE AN OPTION");
            System.out.println("1) Make move");
            System.out.println("2) Quit");
            MENU_CODE = sc.nextByte();
            switch (MENU_CODE) {
                case 1:
                    System.out.println("Enter row and column separated by space");
                    x = sc.nextByte();
                    y = sc.nextByte();
                    if(!makeMove(x, y, currentPlayer)) {
                        System.out.println("Move does not saved, enter correct data");
                    }
                    else {
                        currentPlayer = currentPlayer == Constants.PLAYER_X ? Constants.PLAYER_O : Constants.PLAYER_X;
                    }
                case 2:
                    break;

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
                System.out.println(S.getCell(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    @Override
    public void close() throws SerialPortException {
        S.closeConnector();
        QUIT = true;
    }
}
