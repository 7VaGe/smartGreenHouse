#include "Arduino.h"
#include "config.h"
#include "game_core.h"
#include "user_input.h"

extern byte winner;
extern int nShots;
extern int ledPin[];

void initBall(){
  digitalWrite(ledPin[1],HIGH);
  delay(1000);
}

/*
 * Moving the ball to the specified player
 * with the specified time
 */
byte moveBallTo(byte player, int delayBall){
  #ifdef __DEBUG__
  Serial.println("MOVING TO "+String(player));
  #endif

  /* reset the flags set by the interrupt handlers */
  resetPlayersMovedFlags();
  if (player == 1){
      digitalWrite(ledPin[2],LOW);
      digitalWrite(ledPin[1],HIGH);

      /* delay according to the speed */
      delay(delayBall);

      /* check if any player moved */ 
      byte moves = checkPlayersMoved();
      if (moves != NO_MOVES){
         #ifdef __DEBUG__
         Serial.println("DETECTED PLAYER WHILE MOVING BALL - players: "+String(moves));
         #endif
         return moves;   
      }
      digitalWrite(ledPin[1],LOW);
      digitalWrite(ledPin[0],HIGH);      
  } else {
      digitalWrite(ledPin[0],LOW);
      digitalWrite(ledPin[1],HIGH);
      
      delay(delayBall);      
      
      byte moves = checkPlayersMoved();
      if (moves != NO_MOVES){
         #ifdef __DEBUG__
         Serial.println("EXIT "+String(moves));
         #endif
         return moves;   
      }      
      digitalWrite(ledPin[1],LOW);
      digitalWrite(ledPin[2],HIGH);      
  } 
  return NO_MOVES;
}

bool hasPlayerMoved(byte player, byte moves){
  return (moves & player) != 0x00;
}

/*
 * Check if there is a loser, given the player turn and the moves
 */
byte checkLoser(byte playerTurn, byte moves){
  byte loser = 0;
  if (moves == 0){
    loser = playerTurn;
  } else {
    for (byte i = 1; i <= 2; i++){
      if (hasPlayerMoved(i,moves) && playerTurn != i){
         loser = i;
         break;
      }    
    }
  }
  return loser;
}

/*
 * Main game loop.
 */
void gameLoop(){
  byte loser = NO_LOSERS;
  nShots = 0;
  
  byte playerTurn = random(1,2);
  long   reactTime = INITIAL_RT;

  /* compute the delay of the ball, given the speed */
  int speedVal = analogRead(POT_PIN);
  int speedValInRange = 9*speedVal/1023 + 1;
  int delayBall = 500/speedValInRange; 

  /* looping until someone lose */
  
  while (loser == NO_LOSERS){  

    /* move the ball towards the target player, given by the turn */
    byte moves = moveBallTo(playerTurn,delayBall);

    /* if there are unexpected moves */
    if (moves != NO_MOVES){
      if (hasPlayerMoved(PLAYER1,moves)){
         #ifdef __DEBUG__
         Serial.println("WHILE MOVING BALL Detected player 1");
         #endif
         loser = PLAYER1;
      } else {
         #ifdef __DEBUG__
         Serial.println("WHILE MOVING BALL Detected player 2");
         #endif
         loser = PLAYER2;
      }
    } else {
      nShots++;
      #ifdef __DEBUG__
      Serial.println("WAITING FOR PLAYER: "+String(playerTurn)+"...");
      #endif

      /* Time for the current player to make the move */
      moves = waitPlayersMoveFor(reactTime);
      
      #ifdef __DEBUG__
      Serial.println("detected: "+String(moves));
      #endif      
     
      /* check if there is any loser */
      loser = checkLoser(playerTurn,moves);

      /* if no losers, go with next cycle */
      if (loser == NO_LOSERS){
        playerTurn++;
        if (playerTurn > 2){
          playerTurn = 1; 
        }
        reactTime = reactTime - (reactTime/8);
        
        /* 
         * this is for debouncing the pressed button, 
         * to avoid to detect unexpected moves 
         */
        delay(75);
      }
    }
  }  

  if (loser == PLAYER1){
    winner = PLAYER2;
  } else {
    winner = PLAYER1;
  }
}  
