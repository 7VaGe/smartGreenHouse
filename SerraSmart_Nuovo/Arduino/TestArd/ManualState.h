#ifndef __MANUALSTATE__
#define __MANUALSTATE__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "define.h"
#include "ShareState.h"
#include "ServoIdrante.h"

class ManualState:public Task{

public:
  Led* ledManual;
  Led* ledPump;
  Sonar* proxy;
  ShareState* pState;
  ServoIdrante* Pump;

  ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump);
  void init(int period);
  void tick();
  double mapPump(double val, double valInMin, double valInMax, double valOutMin, double valOutMax);
  
  
};


#endif
