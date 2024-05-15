#include "Led.h"

#define LED_PIN 13

Led led(LED_PIN);

void setup(){
  led.switchOff();
  Serial.begin(9600); 
}

void loop()
{
  if (Serial.available()){
    char data = Serial.read();
    if (data=='1'){
      led.switchOn();  
    } else if (data=='0'){
      led.switchOff();
    }
  }  
}
