#include "ManualState.h"
#include "Define.h"
#include "MsgService.h"

ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, CanaleCom* canale){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale =canale;

}

void ManualState::init(int period){

  Task::init(period);

}

void ManualState::tick(){
    int messaggio = canale->getAndSendMsgBT(); //blueT
    if(messaggio == "e" || proxy->getDistance()<30){
      Serial.println("a");
    }else{
      Serial.println(messaggio);
    }
    int apertura = canale->getValPump();
    this->ledPump->setIntensity(apertura);
    //Pump.write(apertura);
}
