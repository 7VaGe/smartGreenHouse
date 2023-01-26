#ifndef __MANUAL__
#define __MANUAL__

#include "Task.h"
#include "Led.h"
#include "Proxy.h"
#include "Potentiometer.h"

class Manual:public Task{

public:
  Led* led1;
  Led* led2;
  Led* ledm;
  Proxy* sonar;
  Potentiometer* pot;


  Manual(int led1, int led2, int ledm, int echo, int trig, int pot);
  void init(int period);
  void tick();

};


#endif
