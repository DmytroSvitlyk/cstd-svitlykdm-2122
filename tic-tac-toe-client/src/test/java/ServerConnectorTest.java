import jssc.SerialPortException;

public class ServerConnectorTest {

    public static ServerConnector connector;

    public static void main(String[] args) {
        try {
            connector = new ServerConnector(args[0]);
            connectorShouldOpenPort();
            connector.initField();
            connectorShouldOperateWithSells();
            connectorShouldClosePort();
        }  catch (SerialPortException e) {
            System.out.println(e.getPortName() + " " + e.getMethodName() + " " + e.getExceptionType());
        }
    }

    private static void connectorShouldOpenPort() {
        assert connector.portIsOpened();
    }

    private static void connectorShouldGetWinnerIfExists() throws SerialPortException {
        assert connector.checkForWin() == Constants.EMPTY;
        connector.setCell((byte) 0,(byte) 0, Constants.PLAYER_O);
        connector.setCell((byte) 1,(byte) 1, Constants.PLAYER_O);
        connector.setCell((byte) 2,(byte) 2, Constants.PLAYER_O);
        assert connector.checkForWin() == Constants.PLAYER_O;
    }

    private static void connectorShouldClosePort() throws SerialPortException {
        connector.closeConnector();
        assert !connector.getPort().isOpened();
    }

    private static void connectorShouldOperateWithSells() throws SerialPortException {
        connector.setCell((byte) 0, (byte) 0, Constants.PLAYER_X);
        assert connector.getCell((byte) 0, (byte) 0) == Constants.PLAYER_X;
    }

}
