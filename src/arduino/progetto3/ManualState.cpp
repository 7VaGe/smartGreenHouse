#include "ManualState.h"
#include "Define.h"
#include "MsgService.h"
#include "PumpServo.h"

bool mState;
extern bool aState;

ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, CanaleCom* canale, PumpServo* Pump){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->canale =canale;
  this->Pump = Pump;
}

void ManualState::init(int period){

  Task::init(period);

}

void ManualState::tick(){
  if(mState){
    this->ledManual->switchOn();
    int messaggio = canale->getAndSendMsgBT(); //blueT
    if(messaggio == "e" || proxy->getDistance()>30){
      Serial.println("a");
      this->ledManual->switchOff();
      mState=false;
      aState=true;
    }
    if(canale->getMsg()!="niente"){
      int apertura = canale->getValPump();
      this->ledPump->setIntensity(apertura);
      Pump->setAngle(apertura);
    }
  }
}
