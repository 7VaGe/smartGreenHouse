#include "MsgService.h"
#include "Led.h"
#include "ButtonImpl.h"
#include "TempSensor.h"
#include "SoftwareSerial.h"
#include <Wire.h>

/*
 *  BT module connection:  
 *  - pin 2 <=> TXD
 *  - pin 3 <=> RXD
 *
 */ 

MsgService msgService(2,3);
Button* button;
Led* led;
TempSensor* temp;
bool buttonPressed;

void setup() {
  button = new ButtonImpl(4);
  led = new Led(5);
  temp = new TempSensor();
  
  msgService.init();  
  Serial.begin(9600);
  while (!Serial){}
  buttonPressed = false;
  Serial.println("ready to go.");   
}

void loop() {
  if (msgService.isMsgAvailable()) {
    Msg* message = msgService.receiveMsg();
    String msg = message->getContent();
    if (msg == "1"){
      Serial.println("LED ON");  
      led->switchOn(); 
      msgService.sendMsg(Msg("ok"));  
    } else if (msg == "2"){
      Serial.println("LED OFF");    
      led->switchOff(); 
      msgService.sendMsg(Msg("ok"));  
    } else if (msg == "3"){
      int value = temp->readTemperature();
      Serial.println("temp: "+String(value));    
      msgService.sendMsg(Msg("temp:"+String(value)));      
    } 
    delete message;
  }
  
  if (button->isPressed()){
    if (!buttonPressed){
      Serial.println("pressed");
      msgService.sendMsg(Msg("button pressed"));   
      buttonPressed = true;
    }  
  } else if (buttonPressed){
      Serial.println("released");
      msgService.sendMsg(Msg("button released"));      
      buttonPressed = false;
  }
}

