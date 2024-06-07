#include"ServoIdrante.h"
#include"Arduino.h"

ServoIdrante::ServoIdrante(int pin){
  Pump.attach(pin);
}

void ServoIdrante::setAngle(int apertura){
  Pump.write(apertura);
}

void ServoIdrante::closeIdrante(){
  Pump.write(0);
}
