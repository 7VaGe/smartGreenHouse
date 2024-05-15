/*
 * test-pattern-observer-2 example 
 * but using EnableInterrupt lib
 * 
 */
#include "ButtonImpl.h"
#include "Arduino.h"
#include "EnableInterrupt.h"

class Dispatcher {
  public:
    Dispatcher(void (*notifyFunc)()){
      this->notifyFunc = notifyFunc;
    }
    
    void bind(int pin, ButtonImpl* button){
      this->button = button;
      enableInterrupt(pin, notifyFunc, CHANGE);  
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
void notifyButtonListeners_2();
void notifyButtonListeners_3();
void notifyButtonListeners_4();

Dispatcher dispatchers[5] = {
  Dispatcher(notifyButtonListeners_0),
  Dispatcher(notifyButtonListeners_1),
  Dispatcher(notifyButtonListeners_2),
  Dispatcher(notifyButtonListeners_3),
  Dispatcher(notifyButtonListeners_4),
};

void notifyButtonListeners_0(){ dispatchers[0].dispatch(); }
void notifyButtonListeners_1(){ dispatchers[1].dispatch(); }
void notifyButtonListeners_2(){ dispatchers[2].dispatch(); }
void notifyButtonListeners_3(){ dispatchers[3].dispatch(); }
void notifyButtonListeners_4(){ dispatchers[4].dispatch(); }

ButtonImpl::ButtonImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);  
  nListeners = 0;  
  dispatchers[pin].bind(pin, this);
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
