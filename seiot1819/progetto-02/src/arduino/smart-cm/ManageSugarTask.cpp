#include "ManageSugarTask.h"
#include "config.h"
#include "Logger.h"
#include "MsgService.h"

ManageSugarTask::ManageSugarTask(SharedState* pSharedState, Potentiometer* pot, UserConsole* pUserConsole) {
  this->pSharedState = pSharedState;
  this->pPot = pPot;
  this->pUserConsole = pUserConsole;
}
  
void ManageSugarTask::init(int period){
  Task::init(period);
  state = NOT_ACTIVE; 
  currentSugarLevel = pPot->getValue()*100;
  Logger.log("ManageSugarTask->NOT_ACTIVE");
}
  
void ManageSugarTask::tick(){
  switch (state){
    case NOT_ACTIVE: {
      if (pSharedState->isModalityReady()){
        Logger.log("ManageSugarTask->ACTIVE");
        state = ACTIVE;
      }
      break;
    }
    case ACTIVE: {
      if (!pSharedState->isModalityReady()){
        Logger.log("ManageSugarTask->NOT ACTIVE");
        state = NOT_ACTIVE;
      } else {
        int lev = pPot->getValue()*100;
        if (lev < currentSugarLevel - 1 ||  lev > currentSugarLevel + 1){
          Logger.log(String("SugarLevel: ")+lev);
          pSharedState->setSugarLevel(lev);
          pUserConsole->updateSugarLevel(lev);
          currentSugarLevel = lev;
        }
      }
      break;
    }
  }
}
