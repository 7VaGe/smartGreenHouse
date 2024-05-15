#ifndef __USER_INPUT__
#define __USER_INPUT__

#define NO_MOVES 0

void initPlayersInput();
byte waitPlayersMoveFor(long dtime);
byte resetPlayersMovedFlags();
byte checkPlayersMoved();


#endif
