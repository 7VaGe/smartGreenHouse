#include "CanaleServer.h"
#include "Arduino.h"
#include "MsgService.h"

CanaleServer::CanaleServer(){
}

void CanaleServer::sendState(int val){
  if(val==1){
    MsgService.sendMsg("aperta");
  }else{
    MsgService.sendMsg("chiusa");
  }
};

int CanaleServer::getMsg(){
   Msg* msg = MsgService.receiveMsg();
   if(msg!= NULL){
     return msg;
   } else {
     return "niente";
   }
};

int CanaleServer::getValPump(){
   Msg* msg = MsgService.receiveMsg();
   if(msg!= NULL){
    String comunicazione = msg->getContent();
    comunicazione = comunicazione.substring(0,3);
    String appoggio = comunicazione.substring(4);
    int aperturaPompa = appoggio.toInt();
    return aperturaPompa;
   } else {
     return -1;
   }
};
