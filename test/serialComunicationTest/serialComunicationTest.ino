#include "MsgServiceBT.h"
#include "SoftwareSerial.h"
#include "define.h"
#include <LiquidCrystal.h> 

const int RS = 11, EN = 12, D4 = 2, D5 = 3, D6 = 4, D7 = 5;
LiquidCrystal lcd(RS, EN, D4, D5, D6, D7);
/*
 *  BT module connection:  
 *  - RX is pin 2 => to be connected to TXD of BT module
 *  - TX is pin 3 => to be connected to RXD of BT module
 *
 */ 

MsgServiceBT msgServiceBt(2,3);
int count=0;
void setup() {
  MsgService.init();
  msgServiceBt.init();
}

void loop() {
  if (MsgService.isMsgAvailable()){
    Msg* msg = MsgService.receiveMsg();
    if(msg->getContent()=="1"){
      lcd.begin(16, 2);
      lcd.setCursor(0, 0);
      lcd.print(msg->getContent());
      digitalWrite(LEDa, HIGH);
      delay(1000);
      digitalWrite(LEDa, LOW);
      MsgService.sendMsg("Spento");
      }
    if(msg->getContent()=="3"){
      lcd.setCursor(0, 0);
      lcd.print(msg->getContent());
      digitalWrite(LEDp, HIGH);
      delay(1000);
      digitalWrite(LEDp, LOW);
      MsgService.sendMsg("Spento2");
      }
    delete msg;
  }
}
