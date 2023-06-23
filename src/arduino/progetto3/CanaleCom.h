#ifndef __CANALE_COM__
#define __CANALE_COM__

#include "Arduino.h"

class CanaleCom {

public:
  CanaleCom();

  void sendUmidityBT(int val);
  void sendState(int val);
  void sendMessaggio(String val);
  
  int getAndSendMsgBT();
  String getMsg();
  String getMsgBT();
  int getValPump();
  int getStatePump();
};

#endif
