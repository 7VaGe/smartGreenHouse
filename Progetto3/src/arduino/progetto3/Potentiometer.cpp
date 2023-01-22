#include "Potentiometer.h"
#include "Arduino.h"

Potentiometer::Potentiometer(int analogPin) : POT(analogPin){
  pinMode(POT,INPUT);
}

int Potentiometer::getVal(){
  int val = analogRead(POT);
  return val;
}
