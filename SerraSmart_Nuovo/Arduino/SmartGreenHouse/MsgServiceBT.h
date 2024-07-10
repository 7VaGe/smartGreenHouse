#ifndef __MSGSERVICEBT__
#define __MSGSERVICEBT__

#include "Arduino.h"
#include "SoftwareSerial.h"
#include "MsgService.h"

class MsgServiceBT {
    
public:

  MsgServiceBT(int rxPin, int txPin);  
  void init();  
  bool isMsgAvailable();//Function used to know if there are something in the bt channel
  Msg* receiveMsg();//Function used to store the received message, if the isMsgAvaiable on bt channel return true.
  bool sendMsg(Msg msg);//Sending function to send msg through bt channel

private:
  String content;
  SoftwareSerial* channel;
  
};

extern MsgServiceBT MsgBT;

#endif
