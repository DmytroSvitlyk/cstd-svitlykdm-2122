CREATE DATABASE tic_tac_toe;

DROP TABLE IF EXISTS field;
CREATE TABLE field (
    field_row INT,
    field_col INT,
    field_val VARCHAR(1),
    UNIQUE KEY (field_row, field_col)
);

INSERT INTO field (field_row, field_col, field_val) VALUES
    (0, 0, '_'),
    (0, 1, '_'),
    (0, 2, '_'),
    (1, 0, '_'),
    (1, 1, '_'),
    (1, 2, '_'),
    (2, 0, '_'),
    (2, 1, '_'),
    (2, 2, '_');

DROP TABLE IF EXISTS conf;
CREATE TABLE conf (
    game_mode INT,
    curr_player INT,
    player_symbol INT,
    ai_mode INT,
);

INSERT INTO conf (game_mode, curr_player, player_symbol, ai_mode)
VALUES (-1, -1, -1, -1);