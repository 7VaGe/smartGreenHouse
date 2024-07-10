
#include "ManualState.h"
#include "define.h"
#include "MsgService.h"
#include "MsgServiceBT.h"
#include <Arduino.h>

//constructor of Manual state
ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoPump* Pump){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void ManualState::init(int period){
  Task::init(period);
}

//the task ManualState menage the reciving and sending of commands from bluethoot to java server without adding anything, check the distance from sonar and if it is greather than DIST
//the state will change and set it to automatic, if there is a message from serial check the header (with substring separate the header from the data) for the 
//routing of the data (r:send to BT, p:set angle of servo, a:go in automatic).

void ManualState::tick(){
  if(pState->isManual()){
    this->ledManual->switchOn();
    if(MsgBT.isMsgAvailable()){
      Msg* msgBt = MsgBT.receiveMsg();
      String append = msgBt->getContent();
      if(append.length()>0){
        MsgService.sendMsg(append);
        }else{
          MsgService.sendMsg("[ERROR] Void bluethoot message");
        }     
      delete msgBt;
    }
    if(proxy->getDistance()>DIST){
      MsgService.sendMsg("A");
      this->ledManual->switchOff();
      pState->setAutomatic();
      MsgBT.sendMsg(Msg(BTCLOSE));
    }
    if(MsgService.isMsgAvailable()){
       Msg* msg = MsgService.receiveMsg();
       String communication = msg->getContent();
       char header;
       String firstMsg;
       if(communication.length()>0) {
         String headFirstMessage = communication.substring(0,1);
         String communicationNoHeader= communication.substring(1);
         int indexSecondMessage;
         indexSecondMessage =  communicationNoHeader.indexOf(TARGET1);
         if(indexSecondMessage == -1){
            indexSecondMessage = communicationNoHeader.indexOf(TARGET2);
          }
        if (indexSecondMessage != -1) {
          String secondMsg = communicationNoHeader.substring(indexSecondMessage+1);
          String headSecondMessage = communicationNoHeader.substring(indexSecondMessage,(indexSecondMessage + 1));
          header = headSecondMessage[0];
          switch (header){
          case HAUTO:
              pState->setAutomatic();
              this->ledManual->switchOff();
              MsgBT.sendMsg(Msg(BTCLOSE));
              break;
          case HTRACE:
              MsgBT.sendMsg(Msg(secondMsg));
              break;
          case HPUMPSERVO:
              int temp = atoi(secondMsg.c_str());
              temp = map(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
              this->ledPump->setIntensity(temp);
              Pump->setAngle(temp);
              if(temp == PUMP_CLOSE){
                MsgService.sendMsg(CLOSEPUMP);
              }else{
                MsgService.sendMsg(OPENPUMP);
              }
              break;
          }
        }
        header = headFirstMessage[0];
        if(indexSecondMessage != -1){
           firstMsg = communication.substring(1, indexSecondMessage);
        }else{
           firstMsg = communication.substring(1);
        }      
        switch (header){
            case HAUTO:
                pState->setAutomatic();
                this->ledManual->switchOff();
                MsgBT.sendMsg(Msg(BTCLOSE));
                break;
            case HTRACE:
                MsgBT.sendMsg(Msg(firstMsg));
                break;
            case HPUMPSERVO:
                int temp = atoi(firstMsg.c_str());
                temp = map(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
                this->ledPump->setIntensity(temp);
                Pump->setAngle(temp);
                if(temp == PUMP_CLOSE){
                  MsgService.sendMsg(CLOSEPUMP);
                }else{
                  MsgService.sendMsg(OPENPUMP);
                }
                break;
          }
      }else {
        MsgService.sendMsg("[ERROR] Void msg from MsgService");
      }
       delete msg;
    }
  }
}
