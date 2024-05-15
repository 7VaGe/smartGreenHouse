#include "MakingCoffeeTask.h"
#include "config.h"
#include "Logger.h"

MakingCoffeeTask::MakingCoffeeTask(SharedState* pSharedState, ProximitySensor* pProx, Button* pButton, ProgressBar* pBar, UserConsole* pConsole){
  this->pProx = pProx;   
  this->pSharedState = pSharedState;
  this->pButtonT1 = pButton;
  this->pUserConsole = pConsole;
  this->pBar = pBar;
}
  
void MakingCoffeeTask::init(int period){
  Task::init(period);
  state = IDLE; 
  Logger.log("MakingCoffeeTask->IDLE");
  pBar->switchOn();
  pBar->setLevel(0);
}
  
void MakingCoffeeTask::tick(){
  switch (state){
    case IDLE: {
      if (pSharedState->isModalityReady()) {
        Logger.log("MakingCoffeeTask->READY_TO_MAKE");
        state = READY_TO_MAKE;  
      }
      break;
    }
    case READY_TO_MAKE: {
      if (pButtonT1->isPressed()){
        makingCoffeeTime = 0;
        Logger.log("MakingCoffeeTask->MAKING");
        pSharedState->setMakingCoffee();
        pUserConsole->displayMakingCoffee();
        pBar->setLevel(0);
        state = MAKING;
      }
      break;
    }
    case MAKING: {
      makingCoffeeTime += getPeriod();
      if (makingCoffeeTime > DT3) {
        pBar->setLevel(1);
        Logger.log("MakingCoffeeTask->COFFEE_READY");
        pUserConsole->displayCoffeeReady();
        gettingCoffeeTime = 0;
        pSharedState->decNumAvailableCoffee();
        state = COFFEE_READY;
      } else {
        float lev = ((float) makingCoffeeTime) / DT3;
        // Logger.log(String("Coffee stage: ")+lev);
        pBar->setLevel(lev);
      }
      break;
    }
    case COFFEE_READY: {
      float dist = pProx->getDistance();
      gettingCoffeeTime += getPeriod();
      if ((dist < DIST2) || (gettingCoffeeTime > DT4)){
        Logger.log("MakingCoffeeTask->IDLE");
        pBar->setLevel(0);
        pSharedState->setMakingCoffeeCompleted();
        state = IDLE;
      }
      break;
    }
  }
}
