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


}
