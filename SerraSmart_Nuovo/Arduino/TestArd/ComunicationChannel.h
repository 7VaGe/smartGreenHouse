#ifndef __COMUNICATIONCHANNEL__
#define __COMUNICATIONCHANNEL__

#include "Arduino.h"

class ComunicationChannel{
  public:
    ComunicationChannel();
    String getPumpOrder();
};

#endif
