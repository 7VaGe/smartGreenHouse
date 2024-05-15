#include "Scheduler.h"
#include "config.h"
#include "ModManTask.h"
#include "MakingCoffeeTask.h"
#include "MaintenanceTask.h"
#include "ManageSugarTask.h"
#include "Pir.h"
#include "Sonar.h"
#include "Pot.h"
#include "MsgService.h"
#include "Button.h"
#include "ButtonImpl.h"
#include "UserConsole.h"
#include "LedExt.h"
#include "ProgressBar.h"

Scheduler sched;

void setup(){
  
  sched.init(50);

  MsgService.init();
  
  SharedState* pSharedState = new SharedState(MAX_NUM_COFFEE, INIT_SUGAR_LEVEL);
  PresenceSensor* pPres = new Pir(PRES_PIN);
  ProximitySensor* pProx = new Sonar(PROX_ECHO_PIN, PROX_TRIG_PIN);
  Potentiometer* pPot = new Potentiometer(A0);
  Button* pButtonT1 = new ButtonImpl(T1_PIN);
  ProgressBar* pBar = new ProgressBar(new LedExt(L1_PIN), new LedExt(L2_PIN), new LedExt(L3_PIN));
  UserConsole* pUserConsole = new UserConsole();

  /* used for testing  */
  // pSharedState->setModalityReady();
  // pSharedState->setMaintenance();

  Task* t0 = new ModManTask(pSharedState, pPres, pProx, pUserConsole);
  t0->init(200);
  sched.addTask(t0);
 
  Task* t1 = new MakingCoffeeTask(pSharedState, pProx, pButtonT1, pBar, pUserConsole);
  t1->init(50);
  sched.addTask(t1);
  
  Task* t2 = new MaintenanceTask(pSharedState, pUserConsole);
  t2->init(100);
  sched.addTask(t2);
  
  Task* t3 = new ManageSugarTask(pSharedState, pPot, pUserConsole);
  t3->init(100);
  sched.addTask(t3);
}

void loop(){
  sched.schedule();
}
