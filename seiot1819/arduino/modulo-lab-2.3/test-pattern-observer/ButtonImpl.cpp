#include "ButtonImpl.h"
#include "Arduino.h"

ButtonImpl* buttons[2]; 

void notifyButtonListeners_0(){
  buttons[0]->notifyListeners();
}

void notifyButtonListeners_1(){
  buttons[1]->notifyListeners();
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


static void ButtonImpl::notify(){

}

void ButtonImpl::notifyListeners(){
  for (int i = 0; i < nListeners; i++){
    listeners[i]->notifyButtonPressed();
  }  
}

ButtonImpl::ButtonImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);  
  nListeners = 0;  
  registerNewButton(pin,this);
} 
  
bool ButtonImpl::isPressed(){
  return digitalRead(pin) == HIGH;
}

bool ButtonImpl::registerListener(ButtonListener* listener){
  if (nListeners < MAX_BUTTON_LISTENERS){
    listeners[nListeners++] = listener; 
    return true;   
  } else {
    return false; 
  }
}
