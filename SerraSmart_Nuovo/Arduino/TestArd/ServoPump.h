#ifndef __SERVOPUMP__
#define __SERVOPUMP__

#include "ServoTimer2.h"

class ServoPump{
  public:
    ServoPump(int pin);
    void setAngle(int apertura);
    void closeIdrante();
  protected:
    int pin;
    ServoTimer2 Pump;
};

#endif
