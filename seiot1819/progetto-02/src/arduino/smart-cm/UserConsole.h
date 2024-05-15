#ifndef __USER_CONSOLE__
#define __USER_CONSOLE__

class UserConsole {

public:
  UserConsole();

  void displaySleeping();
  void displayReady();
  void displayWelcome();
  void displayMakingCoffee();
  void displayCoffeeReady();  

  void callForMaintenance();
  void updateSugarLevel(int lev);
  void refilled(int nc);

  int checkRefill();

};

#endif
