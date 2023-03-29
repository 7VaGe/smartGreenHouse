#ifndef __AUTOMATIC__
#define __AUTOMATIC__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "Define.h"
#include "CanaleServer.h"

class Automatic:public Task{

public:
  Led* ledAuto;
  Led* ledPump;
  Sonar* proxy;
  CanaleServer* canale;
  
  Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, CanaleServer* canale);  
  void init(int period);  
  void tick();

};


#endif
