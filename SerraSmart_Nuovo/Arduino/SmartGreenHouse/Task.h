#ifndef __TASK__
#define __TASK__

class Task {
  int myPeriod;
  int timeElapsed;
  
public:
  virtual void init(int period){ //Inizialize the task period,setting the value of each tick event to be execute.
    myPeriod = period;  
    timeElapsed = 0; //Time passed from the execution of last task.
  }

  virtual void tick() = 0; //Indicates that this methos is virtual and it need to be implemented to derivates classes, when they call it.
  
  bool updateAndCheckTime(int basePeriod){ //Update the time elapsed, adding the base period as parameter, if timeElapsed is greater than myperiod, the system undestand is time to execute the task.
    timeElapsed += basePeriod;
    if (timeElapsed >= myPeriod){
      timeElapsed = 0;
      return true;
    } else {
      return false; 
    }
  }

  int getPeriod(){//Getter for time period
    return myPeriod;
  }
  
};

#endif
