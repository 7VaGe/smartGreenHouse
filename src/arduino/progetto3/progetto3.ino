#include "MsgService.h"
#include "SoftwareSerial.h"
#include "Sonar.h"
#include "Led.h"
#include "Automatic.h"
#include "ManualState.h"
#include "Timer.h"
#include "Task.h"
#include "Scheduler.h"
#include "CanaleCom.h"
#include "CanaleServer.h"


Scheduler sched;

void setup() {
  sched.init(50);

  MsgService.init();
  CanaleServer* canale = new CanaleServer();

  Sonar* proxy = new Sonar(ECHO, TRIG);
  Led* ledAuto = new Led(LED1);
  Led* ledManual = new Led(LEDm);
  Led* ledPump = new Led(LED2);

  Task* t0 = new Automatic(ledAuto, ledPump, proxy, canale);
  t0->init(50);
  sched.addTask(t0);
 
  Task* t1 = new ManualState(ledManual, ledPump, proxy, canale);
  t1->init(50);
  sched.addTask(t1);
 
}

void loop() {
  sched.schedule();
}
