#ifndef __SERVOIDRANTE__
#define __SERVOIDRANTE__

#include "ServoTimer2.h"

class ServoIdrante{
  public:
    ServoIdrante(int pin);
    void setAngle(int apertura);
    void closeIdrante();
  protected:
    int pin;
    ServoTimer2 Pump;
};

#endif
