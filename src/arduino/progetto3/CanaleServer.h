#ifndef __CANALE_SERVER__
#define __CANALE_SERVER__

class CanaleServer {

public:
  CanaleServer();

  void sendState(int val);

  int getMsg();
  int getValPump();
};

#endif
