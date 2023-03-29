#include "CanaleCom.h"
#include "Arduino.h"
#include "MsgService.h"

CanaleCom::CanaleCom(){
}

void CanaleCom::sendUmidityBT(int val){
  MsgService.sendMsg(String("ciao")+val);
};

void CanaleCom::sendState(int val){
  if(val<0){
    MsgService.sendMsg("aperta");
  }else{
    MsgService.sendMsg("chiusa");
  }
};

int CanaleCom::getMsg() {
    Msg* msg = MsgService.receiveMsg();
    if(msg!= NULL){
      return msg;
    } else {
      return -1;
    }
};

int CanaleCom::getMsgBT() {
    Msg* msg = MsgService.receiveMsg();
    if(msg!= NULL){
      String contenuto = msg->getContent();
      /*String valore = contenuto.substring(4);
      contenuto = contenuto.remove(4);
      contenuto = contenuto.toInt();
      switch(contenuto){
        case O:
          Serial.println("aperta");
          break;
        case W:
          Serial.println("chiusa");
          break;
      }*/
      return 1;
    } else {
      return -1;
    }
    
};
