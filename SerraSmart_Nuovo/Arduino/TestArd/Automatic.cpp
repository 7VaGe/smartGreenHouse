#include "Automatic.h"
#include "define.h"
#include "MsgService.h"
#include "Sonar.h"
#include "MsgServiceBT.h"

Automatic::Automatic(Led* ledAuto, Led* ledPump, Sonar* proxy, ShareState* pState, ServoIdrante* Pump){
  this->ledAuto = ledAuto;
  this->ledPump = ledPump;
  this->proxy = proxy;
  this->pState = pState;
  this->Pump = Pump;
}

void Automatic::init(int period){
  Task::init(period);
}

void Automatic::tick(){
  if(pState->isAutomatico()){
    ledAuto->switchOn();
    if(MsgBT.isMsgAvailable()){
      Msg* msg = MsgBT.receiveMsg();
      String msgFromBT = msg->getContent();
        if (msgFromBT.length()>0) {
              if(msgFromBT =="B" && proxy->getDistance()<DIST){
                MsgService.sendMsg("B");
                MsgBT.sendMsg(Msg(BTOPEN));
              }else{
                MsgBT.sendMsg(Msg(BTCLOSE));
              }
              }else{
                  Serial.println("[DEBUG] Messaggio Bluetooth vuoto ricevuto");
              }
              delete msg;
    }
    if(MsgService.isMsgAvailable()){
      Msg* msg = MsgService.receiveMsg();
      String comunicazione = msg->getContent();
      if (comunicazione.length()>0) {
          // Debug: stampa il messaggio ricevuto      
          String head = comunicazione.substring(0, 1);
          String appoggio = comunicazione.substring(1);        
          // Alloco spazio per il carattere nullo terminatore
          char usable[2];
          head.toCharArray(usable, 2);       
          /* Debug: stampa l'intestazione e il messaggio di appoggio
          Serial.print("[DEBUG] | Intestazione: ");
          Serial.println(usable[0]);
          Serial.print("[DEBUG] | Appoggio: ");
          Serial.println(appoggio);
          /*Invia il messaggio via Bluetooth*/
          MsgBT.sendMsg(Msg(appoggio));
            switch(usable[0]){
              case HMANUAL:
                pState->setManuale();
                this->ledAuto->switchOff();
                break;
              case HPclose:
                this->ledPump->switchOff();
                //MsgService.sendMsg("[LED] | Chiuso");
                Pump->closeIdrante();
                break;
              case HPmin:
                this->ledPump->setIntensity(Pmin);
                Pump->setAngle(PORTATA_MIN);
                //MsgService.sendMsg("[LED] | Caso 3");
                break;
              case HPmed:
                this->ledPump->setIntensity(Pmid);
                //MsgService.sendMsg("[LED] | Caso 2");
                Pump->setAngle(PORTATA_MED);
                break;
              case HPmax:
                this->ledPump->setIntensity(Pmax);
                //MsgService.sendMsg("[LED] | Caso 1");
                Pump->setAngle(PORTATA_MAX);
                break;
              }  } else{
                Serial.println("[DEBUG] Messaggio vuoto ricevuto da MsgService");
              }      
          delete msg;
    }
  }
}
