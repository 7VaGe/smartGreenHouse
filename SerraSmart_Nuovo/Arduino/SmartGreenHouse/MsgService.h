#ifndef __MSGSERVICE__
#define __MSGSERVICE__

#include "Arduino.h"

class Msg {
String content;

public:
  Msg(String content){
    this->content = content;
  }
  
  String getContent(){
    return content;
  }
};

class Pattern {
public:
  virtual boolean match(const Msg& m) = 0;  
};

class MsgServiceClass {
    
public: 
  
  Msg* currentMsg;
  bool msgAvailable;

  void init();  
  
  bool isMsgAvailable(); //Function used to know if there are something in the serial channel
  Msg* receiveMsg(); //Function used to store the received message, if the isMsgAvaiable return true. 

  bool isMsgAvailable(Pattern& pattern);
  Msg* receiveMsg(Pattern& pattern);
  
  void sendMsg(const String& msg); //Sending function to send msg through serial channel
};

extern MsgServiceClass MsgService;

#endif

