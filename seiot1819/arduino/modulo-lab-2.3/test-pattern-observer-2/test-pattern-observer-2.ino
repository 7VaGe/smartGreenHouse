/*
 * test-pattern-observer example 
 * but using a more modular way to
 * dispatch interrupts
 * 
 */
#include "ButtonImpl.h"
 
class MyListener : public ButtonListener {
public:
  MyListener(){
    count = 0;  
  }
  
  void notifyButtonPressed(){
    count++;
  }
  
  int getCount(){
    noInterrupts();
    int c = count;
    interrupts();
    return c;  
  }  
private:
  volatile int count;
};

Button* button;
MyListener* listener;

void setup(){
  Serial.begin(9600);
  button = new ButtonImpl(2);
  listener = new MyListener();
  button->registerListener(listener);
}

void loop(){
  Serial.println(listener->getCount());
};
