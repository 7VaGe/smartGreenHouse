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

class MyListener2 : public ButtonListener {
public:
  MyListener2(){}
  
  void notifyButtonPressed(){
    Serial.println("pressed");
    delay(10000);
    Serial.println("pressed2");
 }
};

Button* button;
MyListener* listener;

void setup(){
  Serial.begin(9600);
  button = new ButtonImpl(2);
  listener = new MyListener();
  button->registerListener(listener);
  button->registerListener(new MyListener2());
}

void loop(){
  Serial.println(listener->getCount());
  delay(1000);
};
