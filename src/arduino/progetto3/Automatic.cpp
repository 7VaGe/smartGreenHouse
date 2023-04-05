#include "Automatic.h"
#include "Define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "CanaleCom.h"

Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, CanaleCom* canale){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale =canale;
};

void Automatic::init(int period){

  Task::init(period);

};

void Automatic::tick(){
    char messaggio = canale->getMsg(); //blue
    if(messaggio=="b" && proxy->getDistance()<30){
      Serial.println("b");
    }
    int apertura = canale->getValPump();
    switch(apertura){
      case 4:
        Serial.println("chiusa");
        this->ledPump->switchOff();
        //Pump.write(0); da risolvere il problema del servo
        break;
      case 3:
        Serial.println("aperta con portata minima");
        this->ledPump->setIntensity(Pmin);
        //Pump.write(Pmin);
        break;
      case 2:
        Serial.println("aperta con portata media");
        this->ledPump->setIntensity(Pmid);
        //Pump.write(Pmid); da risolvere il problema del servo
        break;
      case 1:
        Serial.println("aperta con portata massima");
        this->ledPump->switchOn();
        //Pump.write(Pmax);
        break;
   }
};
