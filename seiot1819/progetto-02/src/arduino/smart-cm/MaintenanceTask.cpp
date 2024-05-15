#include "MaintenanceTask.h"
#include "config.h"
#include "Logger.h"
#include "MsgService.h"

MaintenanceTask::MaintenanceTask(SharedState* pSharedState, UserConsole* pConsole) {
  this->pSharedState = pSharedState;
  this->pUserConsole = pUserConsole;
}
  
void MaintenanceTask::init(int period){
  Task::init(period);
  state = IDLE; 
  Logger.log("MaintenanceTask->IDLE");
}
  
void MaintenanceTask::tick(){
  switch (state){
    case IDLE: {
      if (pSharedState->isInMaintenance()){
        pUserConsole->callForMaintenance();
        state = WAIT_FOR_REFILL;
        Logger.log("MaintenanceTask->REFILL");
      }
      break;
    }
    case WAIT_FOR_REFILL: {
      int nCoffeeRefilled = pUserConsole->checkRefill();
      if (nCoffeeRefilled >= 0){
        Logger.log(String("Refilled: ") + nCoffeeRefilled);
        pSharedState->refillCoffee(nCoffeeRefilled);
        pUserConsole->refilled(nCoffeeRefilled);
        pSharedState->setMaintenanceCompleted();
        Logger.log("MaintenanceTask->IDLE");
        state = IDLE;
      }
      break;
    }
  }
}
