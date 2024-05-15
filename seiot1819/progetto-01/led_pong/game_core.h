#ifndef __GAME_CORE__
#define __GAME_CORE__

#define PLAYER1 1
#define PLAYER2 2
#define NO_LOSERS 0

void initBall();
byte moveBallTo(byte player, int delayBall);
void removeBall();
void gameLoop();

#endif
