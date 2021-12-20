public interface Constants {

    byte PACKAGE_SIZE = 7;
    byte PACKAGE_START = 0;
    byte PACKAGE_CMD = 1;
    byte PACKAGE_PLAYER = 2;
    byte PACKAGE_ROW = 3;
    byte PACKAGE_COLUMN = 4;
    byte PACKAGE_EXT = 5;
    byte PACKAGE_END = 6;

    byte PLAYER_X = 1;
    byte PLAYER_O = 2;
    byte EMPTY = 0;

    byte FIELD_SIZE = 3;

    byte CMD_INIT_FIELD = 0;
    byte CMD_GET_CELL = 1;
    byte CMD_SET_CELL = 2;
    byte CMD_CHECK_WIN = 3;
}
