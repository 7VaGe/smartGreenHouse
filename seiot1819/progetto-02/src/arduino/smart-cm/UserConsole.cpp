#include "UserConsole.h"
#include "Arduino.h"
#include "MsgService.h"

UserConsole::UserConsole(){
}

void UserConsole::displayWelcome() {
  MsgService.sendMsg("cm:we");
};

void UserConsole::displaySleeping() {
  MsgService.sendMsg("cm:zz");
};

void UserConsole::displayReady() {
  MsgService.sendMsg("cm:ok");
};

void UserConsole::displayMakingCoffee() {
	MsgService.sendMsg("cm:mc");
};

void UserConsole::displayCoffeeReady() {
	MsgService.sendMsg("cm:re");
};

void UserConsole::updateSugarLevel(int lev){
    MsgService.sendMsg(String("cm:s|")+lev);
};

void UserConsole::refilled(int nc){
    MsgService.sendMsg(String("cm:f|")+nc);
};

void UserConsole::callForMaintenance() {
	MsgService.sendMsg("cm:ma");
};

int UserConsole::checkRefill() {
    Msg* msg = MsgService.receiveMsg();
    if (msg != NULL){
    	int nc = String(msg->getContent()).toInt();
    	return nc;
    } else {
    	return -1;
    }
};
