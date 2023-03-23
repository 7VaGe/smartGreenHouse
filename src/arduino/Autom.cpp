#include "Manual.h"
#include "Arduino.h"

Autom::Autom(int pot,int echo,int trig,int led1,int led2, int ledm){

  this->sonar   =   new Proxy         (echo,trig);
  this->pot     =   new Potentiometer (pot);
  this->led1    =   new Led        (led1);
  this->led2    =   new Led        (led2);
  this->ledm    =   new Led        (ledm);
}

void Autom::init(int period){

  Task::init(period);

}

void Autom::tick(){
   if(autoState){
    U = pot->getValue();
    Serial.println(U);
    int p=0;
    if(msgBT.isMsgAvailable()){
       Msg* msg = msgBT.receiveMsg();
    }
    if (sonar->getDistance()<DIST && msg->getContent() == 'm'){
      Serial.read()=='0'
      manualState  = true;
      autoState = false;
      this->led1->switchOff();
      this->led2->switchOn();
    }
    switch(Serial.read()){
      case 0:
        Serial.println("chiusa");
        break;
      case 1:
        engine.write(Pmin);
        Serial.println("aperta con portata minima");
        break;
      case 2:
        engine.write(Pmed);
        Serial.println("aperta con portata media");
        break;
      case 3:
        engine.write(Pmax);
        Serial.println("aperta con portata massima");
        break;
      }
    /*if(U >= 20 && U =< 30){
      p=1;
    }
    if(U >= 10 && U =< 20){
      p=2;
    }
    if(U =< 9){
      p=3;
    }
    switch(p){
      case 0:
        Serial.println("chiusa");
        break;
      case 1:
        engine.write(Pmin);
        Serial.println("aperta con portata minima");
        break;
      case 2:
        engine.write(Pmed);
        Serial.println("aperta con portata media");
        break;
      case 3:
        engine.write(Pmax);
        Serial.println("aperta con portata massima");
        break;
      }*/
   }
}
