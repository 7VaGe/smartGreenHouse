#include "Automatic.h"
#include "Define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "CanaleCom.h"

Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, CanaleServer* canale){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale =canale;
};

void Automatic::init(int period){

  Task::init(period);

};

void Automatic::tick(){
    char messaggio = canale->getMsg();
    if(messaggio=="b" && proxy->getDistance()<30){
      Serial.println("b");
    }
    switch(Serial.read()){
      case 4:
        Serial.println("chiusa");
        this->ledPump->switchOff();
        break;
      case 3:
        Serial.println("aperta con portata minima");
        this->ledPump->setIntensity(Pmin);
        break;
      case 2:
        Serial.println("aperta con portata media");
        this->ledPump->setIntensity(Pmid);
        break;
      case 1:
        Serial.println("aperta con portata massima");
        this->ledPump->switchOn();
        break;
   }
};
