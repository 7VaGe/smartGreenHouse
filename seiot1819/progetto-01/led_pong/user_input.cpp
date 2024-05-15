#include "Arduino.h"
#include "config.h"

int buttonPin[] = {T1_PIN, T2_PIN};

bool buttonPressedFlags[] = { false, false};

void button0PressedHandler(){
  buttonPressedFlags[0] = true;      
}

void button1PressedHandler(){
  buttonPressedFlags[1] = true;      
}

void initPlayersInput(){
  for (int i = 0; i < 2; i++){
    pinMode(buttonPin[i], INPUT);     
  } 
  attachInterrupt(digitalPinToInterrupt(buttonPin[0]), button0PressedHandler, RISING); 
  attachInterrupt(digitalPinToInterrupt(buttonPin[1]), button1PressedHandler, RISING); 
}

/*
 * Checks if a player moves using polling
 */
byte waitPlayersMoveFor(long dtime){
  byte player = 0;
  long t0 = millis();
  while (millis() - t0 < dtime){
    for (byte i = 0; i < 2; i++){
      if (digitalRead(buttonPin[i]) == HIGH){
        player = (player | (i + 1));  
      }
    }
    delay(50);
    if (player != 0){
       break;
    }
  }  
  return player;
}

/*
 * Check if a player moved using interrupts
 */
byte checkPlayersMoved(){
  byte player = 0;
  for (byte i = 0; i < 2; i++){
    if (buttonPressedFlags[i]){
      player = (player | (i + 1));  
    }
  }
  return player;
}

/*
 * Reset the flags used by the intterrupt.
 */
byte resetPlayersMovedFlags(){
  noInterrupts();
  for (byte i = 0; i < 2; i++){
    buttonPressedFlags[i] = false;
  }
  interrupts();  
}
