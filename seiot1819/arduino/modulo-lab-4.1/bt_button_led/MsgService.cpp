#include "Arduino.h"
#include "MsgService.h"


MsgService::MsgService(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgService::init(){
  content.reserve(256);
  channel->begin(9600);
}

bool MsgService::sendMsg(Msg msg){
  channel->println(msg.getContent());  
}

bool MsgService::isMsgAvailable(){
  return channel->available();
}

Msg* MsgService::receiveMsg(){
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
