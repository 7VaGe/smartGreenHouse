#ifndef __MANAGE_SUGAR_TASK__
#define __MANAGE_SUGAR_TASK__

#include "Task.h"
#include "SharedState.h"
#include "Pot.h"
#include "UserConsole.h"

class ManageSugarTask: public Task {

private:
  enum { NOT_ACTIVE, ACTIVE } state;
  
  SharedState* pSharedState;
  Potentiometer* pPot;
  UserConsole* pUserConsole;

  int currentSugarLevel;

public:
  ManageSugarTask(SharedState* pState, Potentiometer* pot, UserConsole* pUserConsole);  
  void init(int period);  
  void tick();
};

#endif
