#ifndef __SHARE_STATE__
#define __SHARE_STATE__

class ShareState {

private:
  bool automatico;
  bool manuale;

public:
  
  ShareState(){
    automatico = true;
    manuale = false;
  }
  
  void setAutomatico() { automatico = true; manuale = false; }
  void setManuale() { automatico = false; manuale = true;}
  bool isManuale() { return manuale; }
  bool isAutomatico() { return automatico; }

};

#endif
