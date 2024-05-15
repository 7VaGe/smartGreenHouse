/*
 * Like test-event-loop-2 but 
 * using PIN CHANGE interrupts
 * 
 */
#include "ButtonImpl.h"
#include "Arduino.h"
#include "event.h"
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
      long curr = millis();
      if (curr - last > 50){
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
  dispatchers[pin].bind(pin, this);
} 
  
bool ButtonImpl::isPressed(){
  return digitalRead(pin) == HIGH;
}
