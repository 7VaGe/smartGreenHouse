#ifndef __AUTOM__
#define __AUTOM__

#include "Task.h"
#include "Led.h"
#include "Proxy.h"
#include "Potentiometer.h"

class Autom:public Task{

public:
  Led* led1;
  Led* led2;
  Led* ledm;
  Proxy* sonar;
  Potentiometer* pot;
  
  Autom(int led1, int led2, int ledm, int echo, int trig, int pot);
  void init(int period);
  void tick();

};


#endif
