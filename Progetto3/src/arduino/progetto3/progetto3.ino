#include <Arduino.h>
#include<Servo.h>
#include "Proxy.h" //file.h del sonar
#include "Led.h"
#include "Auto.h"
#include "Manual.h"
#include "Potentiometer.h"
#include "StandBy.h"
#include "Timer.h"
#include "Task.h"
#include "Scheduler.h"

Servo engine;
Timer     timer;
Scheduler sched;


void setup() {
  Serial.begin(9600);

  timer.setupPeriod(50);

  sched.init(50);

  Task* autom  = new Autom(LED1,LED2,LEDM,ECHO,TRIG);
  wait    -> init(50);
  sched.addTask(autom);

  Task* manual = new Manual(LED1,LED2,LEDM,ECHO,TRIG);
  starter -> init(50);
  sched.addTask(manual);

  for(int i = 0; i < CALIBRATION_TIME_SEC; i++){
    delay(1000);
  }

  standByState  = true;

  delay(50);


}

void loop() {
  sched.schedule();
}
