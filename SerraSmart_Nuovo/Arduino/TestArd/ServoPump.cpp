#include"ServoPump.h"
#include"Arduino.h"

ServoPump::ServoPump(int pin){
  Pump.attach(pin);
}

void ServoPump::setAngle(int apertura){
  Pump.write(apertura);
}

void ServoPump::closeIdrante(){
  Pump.write(0);
}
