#include "ManualState.h"
#include "define.h"
#include "MsgService.h"
#include "MsgServiceBT.h"


ManualState::ManualState(Led* ledManual, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump){
  this->ledManual = ledManual;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void ManualState::init(int period){
  Task::init(period);
}

double ManualState::mapPump(double val, double valInMin, double valInMax, double valOutMin, double valOutMax){
  return (val-valInMin)*(valOutMax-valOutMin)/(valInMax-valInMin)+valOutMin;
}

void ManualState::tick(){
  if(pState->isManuale()){
    this->ledManual->switchOn();
    //va inserito lo stesso controllo, quando invio alla pompa il comando
    //di chiusura cosicchè la pompa smette di erogare, e si ferma.
    //al momento anche se io invio un valore non viene eseguito sulla pompa.
    if(MsgBT.isMsgAvailable()){
      Msg* msgBt = MsgBT.receiveMsg();
      appoggio = msgBt->getContent();
      MsgService.sendMsg(appoggio);
    }
    if(proxy->getDistance()>DIST){
      MsgService.sendMsg("A");
      this->ledManual->switchOff();
      pState->setAutomatico();
    }
    if(MsgService.isMsgAvailable()){
       Msg* msg = MsgService.receiveMsg();
       //quando invio un valore da seriale, in modalità manuale, il server mi invia come risposta un messaggio 0.15 esempio, io qui, tolgo il primo carattere e ottengo un .15, che viene inoltrato al BT
       //questo non funziona, va deciso se inserirci un header per farlo lavorare con il codice creato qua, oppure cambiare qui il codice e adattarlo al java.
       String comunicazione = msg->getContent();
       double temp = comunicazione.toDouble();
       temp = mapPump(temp,VAL_START,VAL_STOP,PUMP_CLOSE,PUMP_MAX);
       if(comunicazione==HAUTO){
          pState->setAutomatico();
          this->ledManual->switchOff();
       }else{
          this->ledPump->setIntensity(temp);
          Pump->setAngle(temp); 
       }
       MsgBT.sendMsg(comunicazione);
    }
  }
}
