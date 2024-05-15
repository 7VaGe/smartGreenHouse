#ifndef __MAKING_COFFEE_TASK__
#define __MAKING_COFFEE_TASK__

#include "Task.h"
#include "ProximitySensor.h"
#include "SharedState.h"
#include "Button.h"
#include "UserConsole.h"
#include "ProgressBar.h"

class MakingCoffeeTask: public Task {

private:
  enum { IDLE, READY_TO_MAKE, MAKING, COFFEE_READY } state;

  SharedState* pSharedState;
  ProximitySensor* pProx;
  Button* pButtonT1;
  UserConsole* pUserConsole;
  ProgressBar* pBar;

  int makingCoffeeTime;
  int gettingCoffeeTime;

public:
  MakingCoffeeTask(SharedState* pState, ProximitySensor* pProx, Button* pButtonT1, ProgressBar* pBar, UserConsole* pConsole);  
  void init(int period);  
  void tick();


};

#endif
