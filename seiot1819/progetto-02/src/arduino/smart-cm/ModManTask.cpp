#include "ModManTask.h"
#include "config.h"
#include "Logger.h"
#include <avr/sleep.h>

void wakeUp(){}

ModManTask::ModManTask(SharedState* pSharedState, PresenceSensor* pPres, ProximitySensor* pProx, UserConsole* pUserConsole){
  this->pPres = pPres;
  this->pProx = pProx;   
  this->pSharedState = pSharedState;
  this->pUserConsole = pUserConsole;
}
  
void ModManTask::init(int period){
  Task::init(period);
  attachInterrupt(digitalPinToInterrupt(2), wakeUp, RISING); 
  state = STANDBY; 
  Logger.log("ModManTask->STANDBY");
}
  
void ModManTask::tick(){
  switch (state){
    case STANDBY: {
      pUserConsole->displaySleeping();
      delay(100);
      set_sleep_mode(SLEEP_MODE_PWR_DOWN);  
      sleep_enable();
      sleep_mode();  

      bool det = true; // pPres->isDetected();
      if (det) {
        state = ON; 
        noPresTime = 0;
        pUserConsole->displayReady();
        Logger.log("ModManTask -> ON");
      }

      break;
    }
    case ON: {
      float dist = pProx->getDistance();
      //Logger.log(String("ModManTask - dist: ")+dist);
      noPresTime += getPeriod();      
      if (noPresTime > DT2B) {
        state = STANDBY;
        Logger.log("ModManTask->STANDBY");
      } else if (dist < DIST1) {
        state = READY;
        pSharedState->setModalityReady();
        pUserConsole->displayWelcome();
        Logger.log("ModManTask->READY");
      }
      break;
    }
    case READY: {
      float dist = pProx->getDistance();
      int numAvailCoffee = pSharedState->getNumAvailableCoffee();
      if (numAvailCoffee == 0){
        pSharedState->setMaintenance();
        state = MAINTENANCE;
        Logger.log("ModManTask->MAINTENANCE");
      } else {
        if (dist > DIST1) {
          noProxTime = 0;      
          state = READY_NOONE;
          Logger.log("ModManTask->READY_NOONE");
        } else  if (pSharedState->isMakingCoffee()){
          state = MAKING_COFFEE;
        }
      }
      break;
    }
    case READY_NOONE: {
      float dist = pProx->getDistance();
      if (dist <= DIST1) {
        state = READY;
        Logger.log("ModManTask->READY");
      }
      noProxTime += getPeriod();      
      if (noProxTime > DT2A) {
        state = ON;
        noPresTime = 0;
        pUserConsole->displayReady();
        Logger.log("ModManTask->ON");
      } 
      break;
    }
    case MAKING_COFFEE: {
      // Logger.log("ModManTask in MAKING COFFEE");
      if (!pSharedState->isMakingCoffee()){
        state = READY;
        pSharedState->setModalityReady();
        pUserConsole->displayWelcome();
        Logger.log("ModManTask->READY");
      }
      break;
    }
    case MAINTENANCE: {
      if (!pSharedState->isInMaintenance()){
        state = STANDBY;
        Logger.log("ModManTask->STANDBY");
      }
      break;
    }
  }
}
