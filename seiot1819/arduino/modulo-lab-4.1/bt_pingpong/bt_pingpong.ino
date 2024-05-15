#include "MsgServiceBT.h"
#include "SoftwareSerial.h"

/*
 *  BT module connection:  
 *  - pin 2 <=> TXD
 *  - pin 3 <=> RXD
 *
 */ 
MsgServiceBT msgService(2,3);

void setup() {
  msgService.init();  
  Serial.begin(9600);
  while (!Serial){}
  Serial.println("ready to go."); 
}

void loop() {
  if (msgService.isMsgAvailable()) {
    Msg* msg = msgService.receiveMsg();
    Serial.println(msg->getContent());    
    if (msg->getContent() == "ping"){
       msgService.sendMsg(Msg("pong"));
       delay(500);
    }
    delete msg;
  }
}
