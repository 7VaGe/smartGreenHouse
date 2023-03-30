#ifndef __MANUALSTATE__
#define __MANUALSTATE__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "Define.h"
#include "CanaleCom.h"

class ManualState:public Task{

public:
  Led* ledManual;
  Led* ledPump;
  Sonar* proxy;
  CanaleCom* canale;

  ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, CanaleCom* canale);
  void init(int period);
  void tick();

};


#endif
