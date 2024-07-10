#include"ServoPump.h"
#include"Arduino.h"

ServoPump::ServoPump(int pin){
  Pump.attach(pin);
}

void ServoPump::setAngle(int opening){
  Pump.write(opening);
}

void ServoPump::closePump(){
  Pump.write(0);
}
