#ifndef __PUMPSERVO__
#define __PUMPSERVO__

#include <Servo.h>

class PumpServo{ 
public:
  PumpServo(int pin);
  void setAngle(int val);
  int getAngle();
protected:
  int pin;  
};

extern Servo Pump;

#endif
