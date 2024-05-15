/*
 * PSE - modulo-lab-02 - step #4c - Advanced - Reactive Counting - highlevel
 * 
 */
#include "react.h"
#include "ButtonImpl.h"

volatile int count;
 
void reaction1(Event* ev){
  count++; 
}

void reaction2(Event* ev){
  count+=10; 
}

Button* button;

void setup(){
  Serial.begin(9600);
  button = new ButtonImpl(2);
  addReaction(BUTTON_PRESSED,button,reaction1);
  addReaction(BUTTON_PRESSED,button,reaction2);
}

void loop(){
  Serial.println(count);
};
