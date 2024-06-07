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
       appoggio = msg->getContent();

       if(appoggio.toInt()=="5"){
          pState->setAutomatico();
          this->ledManual->switchOff();
       }else{      
          this->ledPump->setIntensity(appoggio.toInt());
          Pump->setAngle(appoggio.toInt());
          MsgBT.sendMsg(appoggio);
       }
    }
  }
}
