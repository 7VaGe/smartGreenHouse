#ifndef __AUTO__
#define __AUTO__

#include "Task.h"
#include "Led.h"
#include "Proxy.h"

class Auto:public Task{

public:
  Led* led1;
  Led* led2;
  Led* ledm;
  Proxy* sonar;
  Potentiometer* pot;
  
  Auto(int led1, int led2, int ledm, int echo, int trig, int pot);
  void init(int period);
  void tick();

};


#endif
