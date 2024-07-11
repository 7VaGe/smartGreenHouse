#include "MsgService.h"
#include "SoftwareSerial.h"
#include "Sonar.h"
#include "Led.h"
#include "define.h"
#include "AutomaticState.h"
#include "ManualState.h"
#include "Scheduler.h"
#include "ShareState.h"
#include "ServoPump.h"
#include "MsgServiceBT.h"

Scheduler sched;

MsgServiceBT MsgBT(TX,RX);

void setup() {
  sched.init(50);
  MsgService.init();
  MsgBT.init();

  ServoPump* Pump = new ServoPump(PUMP);
  ShareState* pState = new ShareState();
  Sonar* proxy = new Sonar(ECHO, TRIG);
  Led* ledAuto = new Led(LEDa);
  Led* ledManual = new Led(LEDm);
  Led* ledPump = new Led(LEDp);

  Task* t0 = new AutomaticState(ledAuto, ledPump, proxy, pState, Pump);
  t0->init(50);
  sched.addTask(t0);
 
  Task* t1 = new ManualState(ledManual, ledPump, proxy, pState, Pump);
  t1->init(50);
  sched.addTask(t1);

  Pump->setAngle(800);

}

void loop() {
  sched.schedule();
}
