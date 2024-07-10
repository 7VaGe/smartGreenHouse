#ifndef __TIMER__
#define __TIMER__

class Timer {
    
public:  
  Timer();
  /* period in ms */
  void setupPeriod(int period); //set the specific time to generate an interrupt. 
  void waitForNextTick();//wait the timer flag to becoming true, then notify that timer is at the specified value, when tick() will reset the timer flag, then wait for another tick().

};


#endif

