#include "ProgressBar.h"
#include "Arduino.h"

ProgressBar::ProgressBar(LightExt* pL1, LightExt* pL2, LightExt* pL3) {
  currentLevel = 0;
  pLeds[0] = pL1;
  pLeds[1] = pL2;
  pLeds[2] = pL3;
}

void ProgressBar::switchOn(){
  for (int i = 0; i < 3; i++){
    pLeds[i]->switchOn();
  }
}

void ProgressBar::switchOff(){
  for (int i = 0; i < 3; i++){
    pLeds[i]->switchOff();
  }
}

/*
 * setLevel float value - 0 <= value <= 1
 *
 */
void ProgressBar::setLevel(float value) {
  if (value > 0.33){
    pLeds[0]->setIntensity(255);    
    if (value > 0.66) {
       pLeds[1]->setIntensity(255);    
       pLeds[2]->setIntensity((value - 0.66)*255);    
    } else {
       pLeds[1]->setIntensity((value - 0.33)*255);    
       pLeds[2]->setIntensity(0);    
    }
  } else {
     pLeds[0]->setIntensity(value*255);    
     pLeds[1]->setIntensity(0);    
     pLeds[2]->setIntensity(0);    
  }
}
