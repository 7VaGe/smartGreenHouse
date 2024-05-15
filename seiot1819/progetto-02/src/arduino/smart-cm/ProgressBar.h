#ifndef __PROGRESSBAR__
#define __PROGRESSBAR__

#include "LightExt.h"

class ProgressBar { 

public:
  ProgressBar(LightExt* pL1, LightExt* pL2, LightExt* pL3);

  void switchOn();
  void setLevel(float v);
  void switchOff();

private:
  float currentLevel;
  LightExt* pLeds[3];
};

#endif
