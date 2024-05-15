#ifndef __CONFIG__
#define __CONFIG__

#define PROX_TRIG_PIN 7
#define PROX_ECHO_PIN 8
#define PRES_PIN 2

#define POT_PIN A0

/* make coffee button */
#define T1_PIN 6

/* progress bar leds */
#define L1_PIN 3
#define L2_PIN 5
#define L3_PIN 11

/* time to make a coffee */
#define DT3 10000

/* proximity timeout  */
#define DT2A 5000

/* out of distance timeout */
#define DT2B 5000

/* timeout for getting the coffee */
#define DT4 15000

/* welcome distance */
#define DIST1 0.30

/* grab coffee distance*/
#define DIST2 0.10

#define MAX_NUM_COFFEE 5

#define INIT_SUGAR_LEVEL 100

#endif
