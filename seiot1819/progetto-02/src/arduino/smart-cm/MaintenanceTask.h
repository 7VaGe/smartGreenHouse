#ifndef __MAINTENANCE_TASK__
#define __MAINTENANCE_TASK__

#include "Task.h"
#include "SharedState.h"
#include "UserConsole.h"

class MaintenanceTask: public Task {

private:
  enum { IDLE, WAIT_FOR_REFILL } state;
  
  SharedState* pSharedState;
  UserConsole* pUserConsole;

public:
  MaintenanceTask(SharedState* pState, UserConsole* pUserConsole);  
  void init(int period);  
  void tick();
};

#endif
