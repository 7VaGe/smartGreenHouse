#include "MsgServiceBT.h"
#include "SoftwareSerial.h"
#include "define.h"
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
  if (MsgService.isMsgAvailable()){
    Msg* msg = MsgService.receiveMsg();
    if(msg->getContent()=='5'){
      digitalWrite(LEDm, HIGH);
      delay(1000);
      digitalWrite(LEDm, LOW);
      }
    if(msg->getContent()=='6'){
      digitalWrite(LEDa, HIGH);
      delay(1000);
      digitalWrite(LEDa, LOW);
      }
    if(msg->getContent()=='7'){
      digitalWrite(LEDp, HIGH);
      delay(1000);
      digitalWrite(LEDp, LOW);
      }
    delete msg;
  }else{
    MsgService.sendMsg("go");
  }
}
