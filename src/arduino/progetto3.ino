#include <Arduino.h>
#include <Servo.h>
#include "MsgServiceBT.h"
#include "SoftwareSerial.h"
#include "Proxy.h" //file.h del sonar
#include "Led.h"
#include "Autom.h"
#include "Manual.h"
#include "Potentiometer.h"
#include "Timer.h"
#include "Task.h"
#include "Scheduler.h"

MsgServiceBT msgBT(2,3); // RX | TX
Servo engine;
extern bool autoState;
extern bool manualState;
extern char man;
Timer     timer;
Scheduler sched;


void setup() {
  msgBT.init();  
  Serial.begin(9600);
  
  engine.attach(SERVO);

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

  autoState  = true;
  manualState = false;

  delay(50);


}

void loop() {
  sched.schedule();
}
