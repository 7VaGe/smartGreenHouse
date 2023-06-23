#include "PumpServo.h"
#include <Servo.h>

Servo Pump;

PumpServo::PumpServo(int pin){ 
  Pump.attach(pin);
};

void PumpServo::setAngle(int val){
  Pump.write(val);
};

int PumpServo::getAngle(){
  return Pump.read();
};
