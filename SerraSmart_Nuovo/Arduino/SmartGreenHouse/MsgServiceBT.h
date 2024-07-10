#ifndef __MSGSERVICEBT__
#define __MSGSERVICEBT__

#include "Arduino.h"
#include "SoftwareSerial.h"
#include "MsgService.h"

class MsgServiceBT {
    
public:

  MsgServiceBT(int rxPin, int txPin);  
  void init();  
  bool isMsgAvailable();
  Msg* receiveMsg();
  bool sendMsg(Msg msg);

private:
  String content;
  SoftwareSerial* channel;
  
};

extern MsgServiceBT MsgBT;

#endif
