public class PackageTest {

    private static Package pack;

    public static void main(String[] args) {
        pack = new Package();
        pack.getPackage();
    }

    private static void packageShouldInitByteArray() {
        assert pack.getPackage().getClass().isArray();
    }

    private static void packageShouldSetValues() {
        pack.setPlayer(Constants.PLAYER_X);
        pack.setCell((byte) 0, (byte)1);
        assert pack.getPlayer() == Constants.PLAYER_X;
        assert pack.getRow() == 0;
        assert pack.getColumn() == 1;
    }

}
