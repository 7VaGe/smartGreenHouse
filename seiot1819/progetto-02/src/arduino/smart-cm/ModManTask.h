#ifndef __MODMAN_TASK__
#define __MODMAN_TASK__

#include "Task.h"
#include "ProximitySensor.h"
#include "PresenceSensor.h"
#include "SharedState.h"
#include "UserConsole.h"

class ModManTask: public Task {
private:
	
  enum { STANDBY, ON, READY, READY_NOONE, MAKING_COFFEE, MAINTENANCE } state;

  SharedState* pSharedState;
  PresenceSensor* pPres;
  ProximitySensor* pProx;
  UserConsole* pUserConsole;

  int noPresTime;
  int noProxTime;

public:

  ModManTask(SharedState* pState, PresenceSensor* pPres, ProximitySensor* pProc, UserConsole* pUserConsole);  
  void init(int period);  
  void tick();


};

#endif
