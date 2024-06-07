#include "ComunicationChannel.h"
#include "Arduino.h"
#include "MsgService.h"
#include "MsgServiceBT.h"

ComunicationChannel::ComunicationChannel(){
};

String ComunicationChannel::getPumpOrder(){
    Msg* msg = MsgService.receiveMsg();
    if(msg!= NULL){
      String comunicazione = msg->getContent();
      String head = comunicazione.substring(0,1);
      String appoggio = comunicazione.substring(1);
      MsgBT.sendMsg(Msg(appoggio));
      return head;
    }
};
