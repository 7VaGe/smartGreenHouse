/*
 * SEIOT a.a. 2018/2019
 *
 * PROGETTO #1 - LED PONG
 *
 * Esempio di soluzione.
 */
#include "config.h"
#include "game_core.h"
#include "user_input.h"
#include "led_man.h"

/* indicazione del vincitore */ 
byte winner;

/* numero di scambi effettuati */
int nShots;

void setup() {
  initLedBoard();
  initPlayersInput();
  randomSeed(micros());
  Serial.begin(9600);
}

void loop(){
  
  Serial.println("WELCOME to Led Pong. Press Key T3 to Start");
  pulseLed();

  Serial.println("Go!");

  initBall();
  gameLoop();
  resetLedBoard();  
  flashLed(winner);

  Serial.println("Game Over - The Winner is Player "+String(winner)+" after " + String(nShots) + " shots\n");  
}
