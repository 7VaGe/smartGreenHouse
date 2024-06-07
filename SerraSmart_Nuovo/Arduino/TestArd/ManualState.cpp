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
    if(MsgBT.isMsgAvailable()){
      Msg* msgBt = MsgBT.receiveMsg();
      appoggio = msgBt->getContent();
      MsgService.sendMsg(appoggio);
    }
    if(proxy->getDistance()>DIST){
      MsgService.sendMsg("A");
      this->ledManual->switchOff();
      pState->setAutomatico();
    }
    if(MsgService.isMsgAvailable()){
       Msg* msg = MsgService.receiveMsg();
       String comunicazione = msg->getContent();
       String head = comunicazione.substring(0,1);
       appoggio = comunicazione.substring(1);
       if(head==HAUTO){
          pState->setAutomatico();
          this->ledManual->switchOff();
       }else if(head==HPumpServo){
          this->ledPump->setIntensity(appoggio.toInt());
          Pump->setAngle(appoggio.toInt());
       }else{
          MsgBT.sendMsg(Msg(appoggio));
       }
    }
  }
}
