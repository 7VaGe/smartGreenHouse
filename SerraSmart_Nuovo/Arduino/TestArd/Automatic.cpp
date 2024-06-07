#include "Automatic.h"
#include "define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "MsgServiceBT.h"

Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void Automatic::init(int period){
  Task::init(period);
}

void Automatic::tick(){
  if(pState->isAutomatico()){
    ledAuto->switchOn();
    if(MsgBT.isMsgAvailable()){
      Msg* msg = MsgBT.receiveMsg();
      String msgFromBT = msg->getContent(); 
      if(msgFromBT =="B" && proxy->getDistance()<DIST){
        MsgService.sendMsg("B");
      }
      delete msg;
    }
    if(MsgService.isMsgAvailable()){
       Msg* msg = MsgService.receiveMsg();
       String val = msg->getContent();
       MsgBT.sendMsg(Msg(val));
       switch(val.toInt()){
        case 8:
          pState->setManuale();
          this->ledAuto->switchOff();
          break;
        case 4:
          this->ledPump->switchOff();
          MsgService.sendMsg("[LED] | Chiuso");
          Pump->closeIdrante();
          break;
        case 30:
          this->ledPump->setIntensity(Pmin);
          Pump->setAngle(PORTATA_MIN);
          MsgService.sendMsg("[LED] | Caso 3");
          break;
        case 20:
          this->ledPump->setIntensity(Pmid);
          MsgService.sendMsg("[LED] | Caso 2");
          Pump->setAngle(PORTATA_MED);
          break;
        case 10:
          this->ledPump->setIntensity(Pmax);
          MsgService.sendMsg("[LED] | Caso 1");
          Pump->setAngle(PORTATA_MAX);
          break;
        }
       delete msg;
   }
  }
  
}
