#ifndef __SHARED_STATE__
#define __SHARED_STATE__

class SharedState {

private:
  bool makingCoffee;
  bool maintenance;
  int nAvailableCoffee;
  int sugarLevel;
  bool readyModality;

public:
  
  SharedState(int nAvailCoffee, int sugarLevel){
    this->nAvailableCoffee = nAvailCoffee;
    this->sugarLevel = sugarLevel;
    makingCoffee = false;
    maintenance = false;
    readyModality = false;
  }
  
  
  bool isMakingCoffee() { return makingCoffee;  }
  void setMakingCoffee() { makingCoffee = true; readyModality = false; }
  void setMakingCoffeeCompleted() { makingCoffee = false;  }

  bool isInMaintenance() { return maintenance;  }  
  void setMaintenance() { maintenance = true; readyModality = false; }
  void setMaintenanceCompleted() { maintenance = false;  }

  bool isModalityReady() { return readyModality; }
  void setModalityReady() { readyModality = true; }
  void setModalityNotReady() { readyModality = false; }

  int getNumAvailableCoffee() { return nAvailableCoffee; }
  void decNumAvailableCoffee() { nAvailableCoffee--; }
  void refillCoffee(int nc) { nAvailableCoffee+=nc; }

  int getSugarLevel() { return sugarLevel; }
  void setSugarLevel(int level) { sugarLevel = level; }

};

#endif
