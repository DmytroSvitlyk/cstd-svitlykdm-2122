public class Package {

    private static final byte[] PACKAGE = new byte[Constants.PACKAGE_SIZE];

    public Package() {
        PACKAGE[Constants.PACKAGE_START] = (byte) 0xFC;
        PACKAGE[Constants.PACKAGE_END] = (byte) 0xFE;
    }

    public void setPlayer(byte player) {
        PACKAGE[Constants.PACKAGE_PLAYER] = player;
    }

    public void setCommand(byte command) {
        PACKAGE[Constants.PACKAGE_CMD] = command;
    }

    public void setCell(byte x, byte y) {
        PACKAGE[Constants.PACKAGE_ROW] = x;
        PACKAGE[Constants.PACKAGE_COLUMN] = y;
    }

    public byte getPlayer() {
        return PACKAGE[Constants.PACKAGE_PLAYER];
    }

    public byte getRow() {
        return PACKAGE[Constants.PACKAGE_ROW];
    }

    public byte getColumn() {
        return PACKAGE[Constants.PACKAGE_COLUMN];
    }

    public void setPackage(byte[] pack) {
        System.arraycopy(pack, 0, PACKAGE, 0, PACKAGE.length);
    }

    public byte[] getPackage() {
        return PACKAGE;
    }

}
