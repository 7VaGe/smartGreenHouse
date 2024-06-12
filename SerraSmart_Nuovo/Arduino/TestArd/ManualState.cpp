#include "ManualState.h"
#include "define.h"
#include "MsgService.h"
#include "MsgServiceBT.h"


ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void ManualState::init(int period){
  Task::init(period);
}

double ManualState::mapPump(double val, double valInMin, double valInMax, double valOutMin, double valOutMax){
  return (val-valInMin)*(valOutMax-valOutMin)/(valInMax-valInMin)+valOutMin;
}

void ManualState::tick(){
  if(pState->isManuale()){
    this->ledManual->switchOn();
    //va inserito lo stesso controllo, quando invio alla pompa il comando
    //di chiusura cosicchè la pompa smette di erogare, e si ferma.
    //al momento anche se io invio un valore non viene eseguito sulla pompa.
    if(MsgBT.isMsgAvailable()){
      Msg* msgBt = MsgBT.receiveMsg();
      appoggio = msgBt->getContent();
      MsgService.sendMsg(appoggio);
    }
    if(proxy->getDistance()>DIST){
      MsgService.sendMsg("A");
      this->ledManual->switchOff();
      pState->setAutomatico();
      MsgBT.sendMsg(Msg(BTCLOSE));
    }
    if(MsgService.isMsgAvailable()){ 
       Msg* msg = MsgService.receiveMsg();
       String comunicazione = msg->getContent();
       String head = comunicazione.substring(0,1);
       char pivot = head[0];
       appoggio = comunicazione.substring(1);
       switch (pivot){
          case HAUTO:
              pState->setAutomatico();
              this->ledManual->switchOff();
              break;
          case HTrace:
              MsgBT.sendMsg(Msg(appoggio));
              break;
          case HPumpServo:
              int temp = atoi(appoggio.c_str());
              temp = map(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
              this->ledPump->setIntensity(temp);
              Pump->setAngle(temp);
              break;
       }
    }
  }
}
