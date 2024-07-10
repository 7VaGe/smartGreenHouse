#include "Automatic.h"
#include "define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "MsgServiceBT.h"


//constructor of Automatic state
Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, ShareState* pState, ServoPump* Pump){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void Automatic::init(int period){
  Task::init(period);
}

//the starting state of the sistem, in Automatic task we wait for a messagge "B" from bluethoot and the distance from the sonar is fewer to send the request to change state to the server,
//if there is a messagge incoming from serial it separate the first character of the messagge and use it as a header to set the servo angle, or change the state of the sistem to manual.

void Automatic::tick(){
  if(pState->isAutomatic()){
    ledAuto->switchOn();
    if(MsgBT.isMsgAvailable()){
      Msg* msg = MsgBT.receiveMsg();
      String msgFromBT = msg->getContent();
      if(msgFromBT.length()>0){
        if(msgFromBT =="B" && proxy->getDistance()<DIST){
          MsgService.sendMsg("B");
          MsgBT.sendMsg(Msg(BTOPEN));
        }
      }else{
        MsgService.sendMsg("[ERROR] Void bluethoot message");
      }
      delete msg;
    }
    if(MsgService.isMsgAvailable()){
      Msg* msg = MsgService.receiveMsg();
      String comunication = msg->getContent();
      if (comunication.length()>0) {
        String head = comunication.substring(0, 1);
        String append = comunication.substring(1);        
        char usable[2];
        head.toCharArray(usable, 2);
        MsgBT.sendMsg(Msg(append));
        switch(usable[0]){
          case HMANUAL:
            pState->setManual();
            this->ledAuto->switchOff();
            break;
          case HPCLOSE:
            this->ledPump->switchOff();
            Pump->closePump();
            MsgService.sendMsg(CLOSEPUMP);
            break;
          case HPmin:
            this->ledPump->setIntensity(Pmin);
            Pump->setAngle(CAPACITY_MIN);
            MsgService.sendMsg(OPENPUMP);
            break;
          case HPmed:
            this->ledPump->setIntensity(Pmid);
            Pump->setAngle(CAPACITY_MED);
            MsgService.sendMsg(OPENPUMP);
            break;
          case HPmax:
            this->ledPump->setIntensity(Pmax);
            Pump->setAngle(CAPACITY_MAX);
            MsgService.sendMsg(OPENPUMP);
            break;
        }  
      }else{
        MsgService.sendMsg("[ERROR] Void msg from MsgService");
       }      
     delete msg;
    }
  }
}
