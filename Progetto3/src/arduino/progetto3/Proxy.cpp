#include "Proxy.h"
#include "Arduino.h"

float tUS;
float t;
float d;

Proxy::Proxy(int echoPin , int trigPin) : ECHO(echoPin),TRIG(trigPin){
  pinMode(TRIG, OUTPUT);
  pinMode(ECHO, INPUT);
}

float Proxy::getDistance(){

    digitalWrite(TRIG,LOW);
    delayMicroseconds(3);
    digitalWrite(TRIG,HIGH);
    delayMicroseconds(5);
    digitalWrite(TRIG,LOW);

    tUS = pulseIn(ECHO, HIGH);
    t = tUS / 1000.0 / 1000.0 / 2;
    d = t*vs;

    //converto in decimetri
    d=d*10;

    return d;
    
}
