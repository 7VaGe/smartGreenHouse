#include "Auto.h"
#include "Arduino.h"

Manual::Manual(int pot,int echo,int trig,int led1,int led2, int ledm){

  this->sonar   =   new Proxy         (echo,trig);
  this->pot     =   new Potentiometer (pot);
  this->led1    =   new Led        (led1);
  this->led2    =   new Led        (led2);
  this->ledm    =   new Led        (ledm);
}

void Manual::init(int period){

  Task::init(period);

}

void Manual::tick(){

}
