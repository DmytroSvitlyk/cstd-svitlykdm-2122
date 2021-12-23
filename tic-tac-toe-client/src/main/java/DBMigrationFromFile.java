public class DBMigrationFromFile {

    public static void main(String[] args) {
        migrateFromFile(args[0]);
    }

    public static void migrateFromFile(String file) {
        GameSave save;
        try(DBConnector connector = new DBConnector()) {
            save = GameFileIO.loadGame(file);
            connector.writeGameSave(save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
