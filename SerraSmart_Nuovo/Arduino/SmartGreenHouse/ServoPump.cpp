#include"ServoPump.h"
#include"Arduino.h"

//Attach the servo to an analogic pin
ServoPump::ServoPump(int pin){
  Pump.attach(pin);
}

//Set the angle of the servo to a value (750-2250) because we use the ServoTimer2 library
void ServoPump::setAngle(int opening){
  Pump.write(opening);
}

//Set the angle to 0
void ServoPump::closePump(){
  Pump.write(0);
}
