#ifndef __SHARE_STATE__
#define __SHARE_STATE__

//ShareState manage the translation of the system state from automatic to manual, and from manual to automatic state.

class ShareState {

private:
  bool automatic;
  bool manual;

public:
  
  ShareState(){
    automatic = true;
    manual = false;
  }
  
  void setAutomatic() { automatic = true; manual = false; }
  void setManual() { automatic = false; manual = true;}
  bool isManual() { return manual; }
  bool isAutomatic() { return automatic; }

};

#endif
