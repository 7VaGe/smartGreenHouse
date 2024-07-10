
#include "ManualState.h"
#include "define.h"
#include "MsgService.h"
#include "MsgServiceBT.h"
#include <Arduino.h>




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
       if (communication.length()>0) {
        //può capitare che in comunicazione tu abbia 2 messaggi, invati da server, per 2 eventi distinti che si sono verificati nello stesso istante, o circa
        //devi trovare la politica adeguata di troncamento dei messaggi, facendo una ricerca per carattere P o R che differenziano i vari messaggi arrivati da server.
        String headFirstMessage = communication.substring(0,1);
        String communicationNoHeader= communication.substring(1);
        int indexSecondMessage;
        indexSecondMessage =  communicationNoHeader.indexOf(target1);
        if(indexSecondMessage == -1){
          indexSecondMessage = communicationNoHeader.indexOf(target2);
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
          case HTrace:
              MsgBT.sendMsg(Msg(secondMsg));
              break;
          case HPumpServo:
              int temp = atoi(secondMsg.c_str());
              temp = map(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
              this->ledPump->setIntensity(temp);
              Pump->setAngle(temp);
              if(temp == 0){
                MsgService.sendMsg(ClosePump);
              }else{
                MsgService.sendMsg(OpenPump);
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
            case HTrace:
                MsgBT.sendMsg(Msg(firstMsg));
                break;
            case HPumpServo:
                int temp = atoi(firstMsg.c_str());
                temp = map(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
                this->ledPump->setIntensity(temp);
                Pump->setAngle(temp);
                if(temp == PUMP_CLOSE){
                  MsgService.sendMsg(ClosePump);
                }else{
                  MsgService.sendMsg(OpenPump);
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
