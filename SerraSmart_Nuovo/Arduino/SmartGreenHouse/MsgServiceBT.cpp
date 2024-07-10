#include "Arduino.h"
#include "MsgServiceBT.h"


MsgServiceBT::MsgServiceBT(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBT::init(){ 
  content.reserve(256);
  channel->begin(38400);
}

bool MsgServiceBT::sendMsg(Msg msg){
  if(msg.getContent().length()>0){
    channel->println(msg.getContent());
    return true;
  }else{return false;}
  
}

Msg* MsgServiceBT::receiveMsg(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      Msg* msg = new Msg(content);  
      content = ""; 
      return msg; 
    } else {
      content += ch;      
    }
  }
  return NULL;
}

bool MsgServiceBT::isMsgAvailable(){
  return channel->available();
}
