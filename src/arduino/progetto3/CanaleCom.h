#ifndef __CANALE_COM__
#define __CANALE_COM__

class CanaleCom {

public:
  CanaleCom();

  void sendUmidityBT(int val);
  void sendState(int val);

  int getAndSendMsgBT();
  int getMsg();
  int getValPump();
};

#endif
