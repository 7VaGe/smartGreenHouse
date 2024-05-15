#ifndef __BUTTON__
#define __BUTTON__

#include "react.h"

#define BUTTON_PRESSED 1

class Button: public EventSource {
 
public: 
  virtual bool isPressed() = 0;
};

#endif
