 #ifndef __BUTTONIMPL__
#define __BUTTONIMPL__

#include "Button.h"

class ButtonImpl: public Button {
 
public: 
  ButtonImpl(int pin);
  bool isPressed();
  bool registerListener(ButtonListener* listener);

  void notifyListeners();

private:

  int pin;
  int nListeners;
  ButtonListener* listeners[MAX_BUTTON_LISTENERS];
};

#endif
