/*
 * test-pattern-observer example 
 * but using a more modular way to
 * dispatch interrupts
 * 
 */
#include "ButtonImpl.h"
#include "Arduino.h"

class Dispatcher {
  public:
    Dispatcher(void (*notifyFunc)()){
      this->notifyFunc = notifyFunc;
    }
    
    void bind(int intNo, ButtonImpl* button){
      this->button = button;
      attachInterrupt(intNo, notifyFunc, RISING); 
    }

    void dispatch(){
      button->notifyListeners();  
    }

  private:
    ButtonImpl* button;
    void (*notifyFunc)();
};

void notifyButtonListeners_0();
void notifyButtonListeners_1();

Dispatcher dispatchers[2] = {
  Dispatcher(notifyButtonListeners_0),
  Dispatcher(notifyButtonListeners_1)
};

void notifyButtonListeners_0(){
  dispatchers[0].dispatch();
}

void notifyButtonListeners_1(){
  dispatchers[1].dispatch();
}

ButtonImpl::ButtonImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);  
  nListeners = 0;  
  int slot = pin - 2; // pin 2 => slot 0, pin 3 => slot 1
  dispatchers[slot].bind(slot, this);
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

void ButtonImpl::notifyListeners(){
  for (int i = 0; i < nListeners; i++){
    listeners[i]->notifyButtonPressed();
  }  
}
