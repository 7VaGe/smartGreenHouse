#include "MsgService.h"
#include "SoftwareSerial.h"
#include "Sonar.h"
#include "Led.h"
#include "Define.h"
#include "Automatic.h"
#include "ManualState.h"
#include "Timer.h"
#include "Task.h"
#include "Scheduler.h"
#include "CanaleCom.h"
#include "MsgServiceBT.h"
#include "PumpServo.h"
#include <Servo.h>

extern bool aState;
extern bool mState;

Timer timer;

Scheduler sched;

MsgServiceBT MsgBT(3,4);

void setup(){
  timer.setupPeriod(50);
  sched.init(50);
  MsgBT.init();
  MsgService.init();

  char statePos='a';
  
  CanaleCom* canale = new CanaleCom();

  PumpServo* Pump = new PumpServo(POMPA);
  Sonar* proxy = new Sonar(ECHO, TRIG);
  Led* ledAuto = new Led(LEDa);
  Led* ledManual = new Led(LEDm);
  Led* ledPump = new Led(LEDp);

  Task* t0 = new Automatic(ledAuto, ledPump, proxy, canale, Pump);
  t0->init(50);
  sched.addTask(t0);
 
  Task* t1 = new ManualState(ledManual, ledPump, proxy, canale, Pump);
  t1->init(50);
  sched.addTask(t1);

  Pump->setAngle(0);

  aState = true;
  mState = false;
}

void loop() {
  sched.schedule();
}
