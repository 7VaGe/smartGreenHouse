#include "react.h"

#define MAX_REACTIONS 5

class Reaction {
public:
  Reaction(int type, EventSource* pSource, void (*proc)(Event* ev));
  bool isTriggered(Event* ev);
  bool fire(Event* ev);
private:
  int type;
  EventSource* pSource;
   void (*proc)(Event* ev);
};

class ReactionManager {
public:
  static ReactionManager* getInstance();  
  bool addReaction(int event, EventSource* pSource, void (*proc)(Event* ev));  
  void checkReactions(Event* ev);
  
private:
  ReactionManager();  
  
  int nReactions;
  Reaction* pReactions[MAX_REACTIONS];
  
  /* singleton */
  static ReactionManager* instance;
  
};

/* ------------------------ Event  ------------------------ */

Event::Event(int type, EventSource* pSource){
  this->type = type;
  this->pSource = pSource; 
} 
  
int Event::getType(){
  return type;  
}
  
EventSource* Event::getSource(){
  return pSource; 
}

/* ------------------------ Reaction  ------------------------ */

Reaction::Reaction(int type, EventSource* pSource, void (*proc)(Event* ev)){
  this->type = type;
  this->pSource = pSource;
  this->proc = proc;
}
  
bool Reaction::isTriggered(Event* ev){
  if (ev->getType() == this->type && ev->getSource() == this->pSource){
    return true;
  } else {
    return false; 
  }
}
  
bool Reaction::fire(Event* ev){
  (*proc)(ev);
}

/* ------------------------ ReactionManager  ------------------------ */

ReactionManager* ReactionManager::instance = new ReactionManager();

ReactionManager* ReactionManager::getInstance(){
  return instance;
}

ReactionManager::ReactionManager(){
  nReactions = 0;  
}
  
bool ReactionManager::addReaction(int event, EventSource* pSource, void (*proc)(Event* ev)){
  if (nReactions < MAX_REACTIONS){
    pReactions[nReactions] = new Reaction(event, pSource, proc);
    nReactions++;
    return true;
  } else {
    return false;  
  }
}
  
void ReactionManager::checkReactions(Event* ev){
  for (int i = 0; i < nReactions; i++){
    if (pReactions[i]->isTriggered(ev)){
      pReactions[i]->fire(ev);
    }  
  }
}

/* ------------------------ Global  ------------------------ */

bool addReaction(int event, EventSource* pSource, void (*proc)(Event* ev)){
  ReactionManager::getInstance()->addReaction(event, pSource, proc);
}  

void checkReactions(Event* ev){
  ReactionManager::getInstance()->checkReactions(ev);
}





