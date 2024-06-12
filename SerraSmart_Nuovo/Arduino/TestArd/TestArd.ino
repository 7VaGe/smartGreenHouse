#include "MsgService.h"
#include "SoftwareSerial.h"
#include "Sonar.h"
#include "Led.h"
#include "Define.h"
#include "Automatic.h"
#include "ManualState.h"
#include "Scheduler.h"
#include "ShareState.h"
#include "ServoIdrante.h"
#include "MsgServiceBT.h"

Scheduler sched;

MsgServiceBT MsgBT(TX,RX);

void setup() {
  sched.init(50);
  MsgService.init();
  MsgBT.init();

  ServoIdrante* Pump = new ServoIdrante(POMPA);
  ShareState* pState = new ShareState();
  Sonar* proxy = new Sonar(ECHO, TRIG);
  Led* ledAuto = new Led(LEDa);
  Led* ledManual = new Led(LEDm);
  Led* ledPump = new Led(LEDp);

  Task* t0 = new Automatic(ledAuto, ledPump, proxy, pState, Pump);
  t0->init(100);
  sched.addTask(t0);
 
  Task* t1 = new ManualState(ledManual, ledPump, proxy, pState, Pump);
  t1->init(100);
  sched.addTask(t1);

  Pump->setAngle(800);

}

void loop() {
  sched.schedule();
}
