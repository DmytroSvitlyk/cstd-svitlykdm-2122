import jssc.SerialPort;
import jssc.SerialPortException;

public class ServerConnector {

    private static SerialPort PORT;
    private static final Package PACKAGE = new Package();

    public ServerConnector(String port) throws SerialPortException {
        PORT = new SerialPort(port);
        PORT.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        PORT.openPort();
        if(!PORT.isOpened()) {
            throw new SerialPortException(PORT.getPortName(), "ServerConnector Constructor", "Port isn`t open");
        }
    }

    public void initField() throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_INIT_FIELD);
        PORT.writeBytes(PACKAGE.getPackage());
    }

    public byte getCell(byte row, byte column) throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_GET_CELL);
        PACKAGE.setCell(row, column);
        PORT.writeBytes(PACKAGE.getPackage());
        PACKAGE.setPackage(PORT.readBytes(Constants.PACKAGE_SIZE));
        return PACKAGE.getPlayer();
    }

    public void setCell(byte row, byte column, byte player) throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_SET_CELL);
        PACKAGE.setCell(row, column);
        PACKAGE.setPlayer(player);
        PORT.writeBytes(PACKAGE.getPackage());
    }

    public byte checkForWin() throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_CHECK_WIN);
        PORT.writeBytes(PACKAGE.getPackage());
        PACKAGE.setPackage(PORT.readBytes(Constants.PACKAGE_SIZE));
        return PACKAGE.getPlayer();
    }

    public void loadField(byte[][] field) throws SerialPortException {
        for (byte i = 0; i < Constants.FIELD_SIZE; i++) {
            for (byte j = 0; j < Constants.FIELD_SIZE; j++) {
                setCell(i, j, field[i][j]);
            }
        }
    }

    public byte[][] saveField() throws SerialPortException {
        byte[][] f = new byte[Constants.FIELD_SIZE][Constants.FIELD_SIZE];
        for (byte i = 0; i < Constants.FIELD_SIZE; i++) {
            for (byte j = 0; j < Constants.FIELD_SIZE; j++) {
                f[i][j] = getCell(i, j);
            }
        }
        return f;
    }

    public Package getPackage() {
        return PACKAGE;
    }

    public SerialPort getPort() {
        return PORT;
    }

    public boolean portIsOpened() {
        return PORT.isOpened();
    }

    public void closeConnector() throws SerialPortException {
        PORT.closePort();
    }

    public void makeRandomMove(byte player) throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_MAKE_RANDOM_MOVE);
        PACKAGE.setPlayer(player);
        PORT.writeBytes(PACKAGE.getPackage());
    }

    public void makeBestMove(byte player) throws SerialPortException {
        PACKAGE.setCommand(Constants.CMD_MAKE_WIN_MOVE);
        PACKAGE.setPlayer(player);
        PORT.writeBytes(PACKAGE.getPackage());
    }
}
