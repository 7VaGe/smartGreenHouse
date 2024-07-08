#ifndef __MANUALSTATE__
#define __MANUALSTATE__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "define.h"
#include "ShareState.h"
#include "ServoPump.h"

class ManualState:public Task{

public:
  Led* ledManual;
  Led* ledPump;
  Sonar* proxy;
  ShareState* pState;
  ServoPump* Pump;

  ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoPump* Pump);
  void init(int period);
  void tick();
  
  
};


#endif
