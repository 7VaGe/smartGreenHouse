/*
 * Like test-event-loop but with a better modularity
 */
#include "ButtonImpl.h"
#include "Arduino.h"
#include "event.h"

class Dispatcher {
  public:
    Dispatcher(void (*notifyFunc)()){
      this->notifyFunc = notifyFunc;
    }
    
    void bind(int intNo, ButtonImpl* button){
      this->button = button;
      attachInterrupt(intNo, notifyFunc, CHANGE); 
    }

    void dispatch(){
      long curr = millis();
      if (curr - last > 100){
        last = curr;
        Event* ev;
        if (button->isPressed()){
          ev = new ButtonPressed(button);
        } else {
          ev = new ButtonReleased(button);
        }
        dispatchEvent(ev);
      }
    }

  private:
    long last;
    ButtonImpl* button;
    void (*notifyFunc)();
};

void notifyButtonListeners_0();
void notifyButtonListeners_1();

Dispatcher dispatchers[2] = {
  Dispatcher(notifyButtonListeners_0),
  Dispatcher(notifyButtonListeners_1),
};

void notifyButtonListeners_0(){ dispatchers[0].dispatch(); }
void notifyButtonListeners_1(){ dispatchers[1].dispatch(); }


ButtonImpl::ButtonImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);
  int slot = pin - 2;  
  dispatchers[slot].bind(slot, this);
} 
  
bool ButtonImpl::isPressed(){
  return digitalRead(pin) == HIGH;
}
