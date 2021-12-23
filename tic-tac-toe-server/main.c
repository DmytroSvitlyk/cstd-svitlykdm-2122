#include <project.h>

#define PACKET_SIZE 7

#define PACKET_SOP_POS 0
#define PACKET_CMD_POS 1
#define PACKET_D0_POS 2
#define PACKET_D1_POS 3
#define PACKET_D2_POS 4
#define PACKET_D3_POS 5
#define PACKET_EOP_POS 6
#define PACKET_EOP_POS 6

#define PACKET_SOP 0xFC
#define PACKET_EOP 0xFE


#define CMD_INIT_FIELD = 0;
#define CMD_GET_CELL = 1;
#define CMD_SET_CELL = 2;
#define CMD_CHECK_WIN = 3;
#define CMD_MAKE_RANDOM_MOVE = 4;
#define CMD_MAKE_WIN_MOVE = 5;

#define FIELD_SIZE 3

#define EMPTY 0
#define X 1
#define O 2
#define DRAW 3

struct Move
{
    int row, col;
};

char FIELD[FIELD_SIZE][FIELD_SIZE]

uint8 i2cReadBuffer[PACKET_SIZE];
uint8 i2cWriteBuffer[PACKET_SIZE];

char get_cell(char row, char column){
    return FIELD[row][column];
}

void set_cell(char row, char column, char value) {
    FIELD[row][column] = value;
}

void init_default_field() {
	for(int i = 0; i < FIELD_SIZE; i++) {
		for(int j = 0; j < FIELD_SIZE; j++) {
			FIELD[row][column] = 0;
		}
	}
}

char check_win() {
	if((FIELD[0][0] == X && FIELD[0][1] == X && FIELD[0][2] == X) || 
		(FIELD[1][0] == X && FIELD[1][1] == X && FIELD[1][2] == X) ||
		(FIELD[2][0] == X && FIELD[2][1] == X && FIELD[2][2] == X) || 
		(FIELD[0][0] == X && FIELD[1][0] == X && FIELD[2][0] == X) || 
		(FIELD[0][1] == X && FIELD[1][1] == X && FIELD[2][1] == X) || 
		(FIELD[0][2] == X && FIELD[1][2] == X && FIELD[2][2] == X) || 
		(FIELD[0][0] == X && FIELD[1][1] == X && FIELD[2][2] == X) || 
		(FIELD[0][2] == X && FIELD[1][1] == X && FIELD[2][0] == X)) {
			return X;
		}
	if((FIELD[0][0] == O && FIELD[0][1] == O && FIELD[0][2] == O) || 
		(FIELD[1][0] == O && FIELD[1][1] == O && FIELD[1][2] == O) ||
		(FIELD[2][0] == O && FIELD[2][1] == O && FIELD[2][2] == O) || 
		(FIELD[0][0] == O && FIELD[1][0] == O && FIELD[2][0] == O) || 
		(FIELD[0][1] == O && FIELD[1][1] == O && FIELD[2][1] == O) || 
		(FIELD[0][2] == O && FIELD[1][2] == O && FIELD[2][2] == O) || 
		(FIELD[0][0] == O && FIELD[1][1] == O && FIELD[2][2] == O) || 
		(FIELD[0][2] == O && FIELD[1][1] == O && FIELD[2][0] == O)) {
			return O;
		}
	if(!check_moves()) {
	    return DRAW;
	}
	return EMPTY;
}

bool check_moves() {
    for (int i = 0; i < FIELD_SIZE; i++) {
        for (int j = 0; j < FIELD_SIZE; j++) {
            if(FIELD[i][j] == EMPTY) {
                return true;
            }
        }
    }
    return false;
}

int main()
{
    CyGlobalIntEnable;
    
    I2CS_I2CSlaveInitReadBuf (i2cReadBuffer,  PACKET_SIZE);
    I2CS_I2CSlaveInitWriteBuf(i2cWriteBuffer, PACKET_SIZE);
    I2CS_Start();
    
    for (;;)
    {
        if (0u != (I2CS_I2CSlaveStatus() & I2CS_I2C_SSTAT_WR_CMPLT))
        {
            if (PACKET_SIZE == I2CS_I2CSlaveGetWriteBufSize())
            {
                if (
                    (i2cWriteBuffer[PACKET_SOP_POS] == PACKET_SOP)
                    && (i2cWriteBuffer[PACKET_EOP_POS] == PACKET_EOP)
                )
                {
                    switch(i2cWriteBuffer[PACKET_CMD_POS]){
                    case CMD_GET_CELL:
                        i2cReadBuffer[PACKET_D0_POS] = get_cell(i2cWriteBuffer[PACKET_D1_POS], i2cWriteBuffer[PACKET_D2_POS]);
                        break;
                    case CMD_SET_SELL:
                        set_cell(i2cWriteBuffer[PACKET_D1_POS], i2cWriteBuffer[PACKET_D2_POS], i2cWriteBuffer[PACKET_D0_POS]);
                        break;
					case CMD_CHECK_WIN:
                        i2cReadBuffer[PACKET_D0_POS] = check_win();
                        break;
					case CMD_INIT_FIELD:
						init_default_field();
						break;
					case CMD_MAKE_RANDOM_MOVE:
					    char player = i2cWriteBuffer[PACKET_D0_POS];
					    char opponent = player == X ? O : X;
					    make_random(player, opponent);
						break;
                    case CMD_MAKE_WIN_MOVE:
                        char player = i2cWriteBuffer[PACKET_D0_POS];
                    	char opponent = player == X ? O : X;
                        Move bestMove = findBestMove(player, opponent);
                        set_cell(bestMove.row, bestMove.column, player);
                        break;
                    }
                }
            }
            
            I2CS_I2CSlaveClearWriteBuf();
            (void) I2CS_I2CSlaveClearWriteStatus();
        }
        
        if (0u != (I2CS_I2CSlaveStatus() & I2CS_I2C_SSTAT_RD_CMPLT))
        {
            I2CS_I2CSlaveClearReadBuf();
            (void) I2CS_I2CSlaveClearReadStatus();
        }
    }
}

void make_random(char player) {
    char x, y;
    while(true) {
        x = rand() % 3;
        y = rand() % 3;
        if(FIELD[x][y] == EMPTY) {
            FIELD[x][y] = player;
            break;
        }
    }
}

int evaluate(char b[3][3], char player, char opponent)
{
    for (int row = 0; row<3; row++)
    {
        if (b[row][0]==b[row][1] &&
            b[row][1]==b[row][2])
        {
            if (b[row][0]==player)
                return +10;
            else if (b[row][0]==opponent)
                return -10;
        }
    }
    for (int col = 0; col<3; col++)
    {
        if (b[0][col]==b[1][col] &&
            b[1][col]==b[2][col])
        {
            if (b[0][col]==player)
                return +10;

            else if (b[0][col]==opponent)
                return -10;
        }
    }
    if (b[0][0]==b[1][1] && b[1][1]==b[2][2])
    {
        if (b[0][0]==player)
            return +10;
        else if (b[0][0]==opponent)
            return -10;
    }
    if (b[0][2]==b[1][1] && b[1][1]==b[2][0])
    {
        if (b[0][2]==player)
            return +10;
        else if (b[0][2]==opponent)
            return -10;
    }
    return 0;
}

int minimax(int depth, bool isMax, char player, char opponent)
{
    int score = evaluate(player, opponent);
    if (score == 10)
        return score;
    if (score == -10)
        return score;
    if (!check_moves())
        return 0;
    if (isMax)
    {
        int best = -1000;

        for (int i = 0; i<3; i++)
        {
            for (int j = 0; j<3; j++)
            {
                if (board[i][j]==EMPTY)
                {
                    board[i][j] = player;
                    best = max(best, minimax(depth+1, !isMax, player, opponent));
                    board[i][j] = EMPTY;
                }
            }
        }
        return best;
    }
    else
    {
        int best = 1000;

        for (int i = 0; i<3; i++)
        {
            for (int j = 0; j<3; j++)
            {
                if (board[i][j]==EMPTY)
                {
                    board[i][j] = opponent;
                    best = min(best, minimax(depth+1, !isMax, player, opponent));
                    board[i][j] = EMPTY;
                }
            }
        }
        return best;
    }
}
Move findBestMove(char player, char opponent)
{
    int bestVal = -1000;
    Move bestMove;
    bestMove.row = -1;
    bestMove.col = -1;

    for (int i = 0; i<3; i++)
    {
        for (int j = 0; j<3; j++)
        {
            if (board[i][j]==EMPTY)
            {
                board[i][j] = player;
                int moveVal = minimax(0, false, player, opponent);
                board[i][j] = EMPTY;
                if (moveVal > bestVal)
                {
                    bestMove.row = i;
                    bestMove.col = j;
                    bestVal = moveVal;
                }
            }
        }
    }
    return bestMove;
}