#include "SoftwareSerial.h"
#include <Wire.h>

/*
 *  BT module connection:  
 *  - pin 2 <=> TXD
 *  - pin 3 <=> RXD
 *
 */ 

#define TX_PIN 2
#define RX_PIN 3
#define LED_PIN 13

SoftwareSerial channel(RX_PIN, RX_PIN);

void setup() {
  pinMode(LED_PIN,OUTPUT);
  channel.begin(9600);
  
  Serial.begin(9600);
  while (!Serial){}
  Serial.println("ready to go.");   
}

void loop() {
  if (channel.available()) {
    String msg="";
    while (channel.available()) {
      msg += (char) channel.read();      
    }    
    if (msg == "1"){
      Serial.println("LED ON");  
      digitalWrite(LED_PIN,HIGH);
      channel.println("ok");  
    } else if (msg == "2"){
      Serial.println("LED OFF");    
      digitalWrite(LED_PIN,LOW);
      channel.println("ok");  
    } 
  }
}

