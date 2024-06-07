#ifndef __AUTOMATIC__
#define __AUTOMATIC__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "define.h"
#include "ShareState.h"
#include "ServoIdrante.h"

class Automatic:public Task{

public:
  Led* ledAuto;
  Led* ledPump;
  Sonar* proxy;
  ShareState* pState;
  ServoIdrante* Pump;
  
  Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump);  
  void init(int period);  
  void tick();

};


#endif
