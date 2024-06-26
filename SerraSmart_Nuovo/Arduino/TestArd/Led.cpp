#include "Led.h"
#include "Arduino.h"

Led::Led(int pin){
  this->pin = pin;
  pinMode(pin,OUTPUT);
}

void Led::switchOn(){
  digitalWrite(pin,HIGH);
}

void Led::switchOff(){
  digitalWrite(pin,LOW);
}

void Led::setIntensity(int val){
    analogWrite(pin,val);
};

int Led::getIntensity(){
   return digitalRead(pin);
};

