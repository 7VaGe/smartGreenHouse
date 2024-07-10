#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Timer.h"
#include "Task.h"

#define MAX_TASKS 10

class Scheduler {
  
  int basePeriod;
  int nTasks;
  Task* taskList[MAX_TASKS];  
  Timer timer;

public:
  void init(int basePeriod);  //Initialize the schedluer time to basePeriod and configures timer as well.
  virtual bool addTask(Task* task);  //This function add a new task on the task list, if the list is not empty the task will be add, and the counter task will be increased.
  virtual void schedule();// this is a periodic funcion used to wait the next timer tick, and next will check all the task contained in the task list, if the task need to be esecuted, will call tick().
};

#endif

