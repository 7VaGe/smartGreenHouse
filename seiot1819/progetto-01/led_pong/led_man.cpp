#include "Arduino.h"
#include "config.h"
#include "game_core.h"

int ledPin[] = {LED01_PIN, LED02_PIN, LED03_PIN};

void initLedBoard(){   
  for (int i = 0; i < 3; i++){
    pinMode(ledPin[i], OUTPUT);     
  }
  pinMode(PULSELED_PIN,OUTPUT);  
}

void resetLedBoard(){   
  for (int i = 0; i < 3; i++){
    digitalWrite(ledPin[i], LOW);     
  }
}

/*
 * Pulse the led until the T3 button is pressed.
 * 
 */
void pulseLed(){
  int currIntensity = 0;
  int fadeAmount = 5;
  int pressed = digitalRead(T3_PIN);
  while (pressed == LOW){    
    analogWrite(PULSELED_PIN, currIntensity);   
    currIntensity = currIntensity + fadeAmount;
    if (currIntensity == 0 || currIntensity == 255) {
      fadeAmount = -fadeAmount ; 
    }     
    pressed = digitalRead(T3_PIN);
    delay(20);
  }  
  analogWrite(PULSELED_PIN, 0);   
}


/*
 * Flash the led of the winner player.
 */
void flashLed(byte winner){
  byte led = 0;
  if (winner == PLAYER1){
    led = 0;
  } else {
    led = 2;  
  }
  for (int i = 0; i < 10; i++){
    digitalWrite(ledPin[led],HIGH);
    delay(100);  
    digitalWrite(ledPin[led],LOW);
    delay(100);    
  }
}
