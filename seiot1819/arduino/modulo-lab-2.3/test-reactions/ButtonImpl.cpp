#include "ButtonImpl.h"
#include "Arduino.h"
#include "react.h"

ButtonImpl* buttons[2]; 

void notifyButtonListeners_0(){
  if (buttons[0] != NULL){
    Event* ev = new Event(BUTTON_PRESSED, buttons[0]);
    checkReactions(ev);
    delete ev;
  }
}

void notifyButtonListeners_1(){
  if (buttons[1] != NULL){
    Event* ev = new Event(BUTTON_PRESSED, buttons[1]);
    checkReactions(ev);
    delete ev;
  }
}

void registerNewButton(int pin, ButtonImpl* button){
  switch (pin){
    case 2: buttons[0] = button;
            attachInterrupt(0, notifyButtonListeners_0, RISING); 
            break;
    case 3: buttons[1] = button;
            attachInterrupt(1, notifyButtonListeners_1, RISING); 
            break;
  } 
}


ButtonImpl::ButtonImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);  
  registerNewButton(pin,this);
} 
  
bool ButtonImpl::isPressed(){
  return digitalRead(pin) == HIGH;
}


