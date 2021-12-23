import java.sql.*;

public class DBConnector implements AutoCloseable {

    public static final String CONN = "jdbc:mysql://localhost:3306/test";
    public static final String USER = "root";
    public static final String PASS = "123";
    private static final String FIELD_SELECT = "SELECT * FROM field";
    private static final String CONF_SELECT = "SELECT * FROM conf";
    private static final String FIELD_UPDATE = "UPDATE field SET val = ? WHERE field_row = ? AND field_col = ?";
    private static final String CONF_UPDATE = "UPDATE field SET game_mode = ?, curr_player = ?, player_symbol = ?, ai_mode = ?";

    private static Connection con;


    public DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(CONN, USER, PASS);
    }

    private byte stringCellToByte(String cell) {
        return cell.equals("_") ? Constants.EMPTY : (cell.equals("X") ? Constants.PLAYER_X : Constants.PLAYER_O);
    }

    private String byteCellToString(byte cell) {
        return cell == Constants.EMPTY ? "_" : (cell == Constants.PLAYER_X ? "X" : "O");
    }

    public void writeGameSave(GameSave save) throws SQLException {
        con.setAutoCommit(false);
        try(PreparedStatement field = con.prepareStatement(FIELD_UPDATE);
            PreparedStatement conf = con.prepareStatement(CONF_UPDATE)) {
            for (byte i = 0; i < Constants.FIELD_SIZE; i++) {
                for (byte j = 0; j < Constants.FIELD_SIZE; j++) {
                    field.setString(1, byteCellToString(save.getField()[i][j]));
                    field.setInt(2, i);
                    field.setInt(3, j);
                    field.executeUpdate();
                }
            }
            conf.setInt(1, save.getGameMode());
            conf.setInt(2, save.getCurrentPlayer());
            if(save.getGameMode() == Constants.MAN_VS_AI_MODE) {
                conf.setInt(3, save.getPlayerSymbol());
                conf.setInt(4, save.getAiMode());
            } else {
                conf.setInt(3, -1);
                conf.setInt(4, -1);
            }
            conf.executeUpdate();
        } finally {
            con.setAutoCommit(false);
        }
        con.setAutoCommit(true);
    }

    public GameSave readGameSave() throws SQLException {
        GameSave save = new GameSave();
        byte row, column, val;
        byte count = 0;
        try(ResultSet set = con.createStatement().executeQuery(FIELD_SELECT)) {
            while (set.next()) {
                row = set.getByte(1);
                column = set.getByte(2);
                val = stringCellToByte(set.getString(3));
                save.getField()[row][column] = val;
                count++;
            }
            if(count != 9) {
                System.out.println("Save DB is corrupted, try to rebuild database");
                return save;
            }
        }
        try(ResultSet set = con.createStatement().executeQuery(CONF_SELECT)) {
            if(set.next()) {
                save.setGameMode(set.getByte("game_mode"));
                save.setCurrentPlayer(set.getByte("curr_player"));
                if(save.getGameMode() == Constants.MAN_VS_AI_MODE) {
                    save.setCurrentPlayer(set.getByte("player_symbol"));
                    save.setAiMode(set.getByte("ai_mode"));
                }
            } else {
                System.out.println("Save DB is corrupted, try to rebuild database");
            }
        }
        return save;
    }

    @Override
    public void close() throws Exception {
        con.close();
    }
}
