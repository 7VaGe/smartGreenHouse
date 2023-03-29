#include "ManualState.h"
#include "Define.h"
#include "MsgService.h"

ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, CanaleServer* canale){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale =canale;

}

void ManualState::init(int period){

  Task::init(period);

}

void ManualState::tick(){
    int messaggio = canale->getMsg(); //blueT
    if(messaggio == "e" || proxy->getDistance()<30){
      Serial.println("a");
    }
    int azione = canale->getMsg();
    this->ledPump->setIntensity(azione);
}
