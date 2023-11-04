#include "Automatic.h"
#include "Define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "CanaleCom.h"
#include "PumpServo.h"

Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, CanaleCom* canale, PumpServo* Pump){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale = canale;
  this->Pump = Pump;
};

void Automatic::init(int period){

  Task::init(period);

};

void Automatic::tick(){
    this->ledAuto->switchOn();
    String messaggio = canale->getMsgBT();
    if(messaggio=="b" && proxy->getDistance()<30){
      Serial.println("b");
      this->ledAuto->switchOff();
    }
    int apertura = canale->getValPump();
    switch(apertura){
      case 4:
        Serial.println("chiusa");
        this->ledPump->switchOff();
        Pump->setAngle(0);
        break;
      case 3:
        Serial.println("aperta con portata minima");
        this->ledPump->setIntensity(Pmin);
        Pump->setAngle(Pmin);
        break;
      case 2:
        Serial.println("aperta con portata media");
        this->ledPump->setIntensity(Pmid);
        Pump->setAngle(Pmid);
        break;
      case 1:
        Serial.println("aperta con portata massima");
        this->ledPump->switchOn();
        Pump->setAngle(Pmax);
        break;
   }
};
