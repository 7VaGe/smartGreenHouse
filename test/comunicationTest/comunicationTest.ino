#include "MsgServiceBT.h"
#include "SoftwareSerial.h"

/*
 *  BT module connection:  
 *  - RX is pin 2 => to be connected to TXD of BT module
 *  - TX is pin 3 => to be connected to RXD of BT module
 *
 */ 
MsgServiceBT msgServiceBt(2,3);

void setup() {
  MsgService.init();
  msgServiceBt.init();
}

void loop() {
  if (msgServiceBt.isMsgAvailable()) {
    Msg* msg = msgServiceBt.receiveMsg();
    MsgService.sendMsg(msg->getContent());
    delay(1000);
    delete msg;
  }
  if (MsgService.isMsgAvailable()){
    Msg* msg = MsgService.receiveMsg();
    msgServiceBt.sendMsg(msg->getContent());
    delay(1000);
    delete msg;
  }
}
