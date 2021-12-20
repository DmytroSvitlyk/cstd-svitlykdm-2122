#include <project.h>

#define PACKET_SIZE 7

#define PACKET_SOP_POS 0
#define PACKET_CMD_POS 1
#define PACKET_D0_POS 2
#define PACKET_D1_POS 3
#define PACKET_D2_POS 4
#define PACKET_D3_POS 5
#define PACKET_EOP_POS 6

#define PACKET_SOP 0xFC
#define PACKET_EOP 0xFE


#define CMD_INIT_FIELD = 0;
#define CMD_GET_CELL = 1;
#define CMD_SET_CELL = 2;
#define CMD_CHECK_WIN = 3;

#define FIELD_SIZE 3

#define EMPTY 0
#define X 1
#define O 2

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
	return EMPTY
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
                        i2cReadBuffer[PACKET_D0_POS] = get_cell(i2cWriteBuffer[PACKET_D0_POS], i2cWriteBuffer[PACKET_D1_POS], i2cWriteBuffer[PACKET_D2_POS]);
                        break;
                    case CMD_SET_SELL:
                        set_cell(i2cWriteBuffer[PACKET_D0_POS], i2cWriteBuffer[PACKET_D1_POS], i2cWriteBuffer[PACKET_D2_POS], i2cWriteBuffer[PACKET_D3_POS]);
                        break;
					case CMD_CHECK_WIN:
                        i2cReadBuffer[PACKET_D0_POS] = check_win(i2cWriteBuffer[PACKET_D0_POS], i2cWriteBuffer[PACKET_D1_POS], i2cWriteBuffer[PACKET_D2_POS]);
                        break;
					case CMD_INIT_FIELD:
						init_default_field();
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