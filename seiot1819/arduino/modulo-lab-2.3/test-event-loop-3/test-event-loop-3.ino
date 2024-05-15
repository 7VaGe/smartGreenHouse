/*
 * Like test-event-loop-2 but 
 * using PIN CHANGE interrupts
 * 
 */
#include "event.h"
#include "button.h"
#include "buttonImpl.h"

Button* button;

int count = 0;

void buttonPressedHandler(Event* ev){
  Serial.println("pressed");
  count = count + 1;
  Serial.println(count);
}

void buttonReleasedHandler(Event* ev){
  Serial.println("released");
  count = count - 1;
  Serial.println(count);
}

void setup() {
  Serial.begin(9600);  
  button = new ButtonImpl(4);
  addEventHandler(BUTTON_PRESSED_EVENT, buttonPressedHandler);  
  addEventHandler(BUTTON_RELEASED_EVENT, buttonReleasedHandler);  
}

void loop() {
  mainEventLoop();
}
