#ifndef __PROXY__
#define __PROXY__

class Proxy {

public:
  Proxy(int ECHO, int TRIG);
  float getDistance();

private:
    const float vs = 331.5 + 0.6*20;
    int ECHO, TRIG;

};

#endif
