#include "CanaleCom.h"
#include "Arduino.h"
#include "MsgService.h"
#include "MsgServiceBT.h"

CanaleCom::CanaleCom(){
}

void CanaleCom::sendUmidityBT(int val){
  MsgBT.sendMsg(String("U")+val);
};

void CanaleCom::sendState(int val){
  if(val<0){
    MsgService.sendMsg("aperta");
  }else{
    MsgService.sendMsg("chiusa");
  }
};

int CanaleCom::getAndSendMsgBT() {
   Msg* msg = MsgBT.receiveMsg(); //blue
   if(msg!= NULL){
     MsgService.sendMsg(msg->getContent());
     return 1;
   } else {
     return 0;
   }
};

void sendMessaggio(String val){
  MsgService.sendMsg(val);
};

String CanaleCom::getMsg(){
   Msg* msg = MsgService.receiveMsg();
   if(msg!= NULL){
     return msg->getContent();
   } else {
     return "niente";
   }
};

String CanaleCom::getMsgBT(){
   Msg* msg = MsgBT.receiveMsg();
   if(msg!= NULL){
     return msg->getContent();
   } else {
     return "niente";
   }
};

int CanaleCom::getValPump(){
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

int CanaleCom::getStatePump(){
   Msg* msg = MsgService.receiveMsg();
   if(msg!= NULL){
    String comunicazione = msg->getContent();
    comunicazione = comunicazione.substring(0,3);
    if(comunicazione == "BWO"){
    return 1;
   } else {
     return 0;
   }
 }
};
