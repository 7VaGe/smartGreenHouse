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
  /*
    * Optional system state, that work with user commands.
    *
    * @param ledAuto blue led used to indicates that the system is in automatic state
    * @param ledPump  green led used to indicate that the system is supplying water
    * @param proxy sonar module that allow the system to know the distance from bluetooth devices, or others devices
    * @param pState current pump state
    * @param Pump servo module that simulate a smarth green house.
    */
  ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoPump* Pump);
  /*
    * Timer period, passed to the scheduler to perform task
    *
    * @param period the time period.
  */
  void init(int period);
  /*
    * Task operation explained on manual.cpp file 
    */
  void tick();
  
  
};


#endif
