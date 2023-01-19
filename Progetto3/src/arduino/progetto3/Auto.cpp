#include "Manual.h"
#include "Arduino.h"

Auto::Auto(int pot,int echo,int trig,int led1,int led2, int ledm){

  this->sonar   =   new Proxy         (echo,trig);
  this->pot     =   new Potentiometer (pot);
  this->led1    =   new Led        (led1);
  this->led2    =   new Led        (led2);
  this->ledm    =   new Led        (ledm);
}

void Auto::init(int period){

  Task::init(period);

}

void Auto::tick(){
  this->led1->switchOn();
  U = analogRead(POT);
  if(U<Umin){
    
  }
}
