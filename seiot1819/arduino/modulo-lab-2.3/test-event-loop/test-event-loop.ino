
#include "event.h"
#include "button.h"
#include "buttonImpl.h"

Button* button;

int count = 0;

void buttonPressedHandler(Event* ev){
  count = count + 1;
  Serial.println(count);
}

void setup() {
  Serial.begin(9600);  
  button = new ButtonImpl(2);
  addEventHandler(BUTTON_PRESSED_EVENT, buttonPressedHandler);  
}

void loop() {
  mainEventLoop();
}
