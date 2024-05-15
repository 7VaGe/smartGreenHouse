#ifndef __REACT__
#define __REACT__

/* ------------------------ EventSource  ------------------- */

class EventSource {
};

/* ------------------------ Event  ------------------------ */

class Event {
public:

  /**
   * An event, characterized by a type and a source.
   * 
   */
  Event(int type, EventSource* pSource);

  /* get the type of the event */
  int getType();

  /* get the source of the event */
  EventSource* getSource();
private:
  int type;
  EventSource* pSource;
};

/*
 * Add a new reaction 
 * 
 * - event - type of the event 
 * - pSource* - source of the event
 * - proc - reaction body
 */
bool addReaction(int event, EventSource* pSource, void (*proc)(Event* ev));  

/*
 * Called by observed devices to notify reactions
 *
 */
void checkReactions(Event* ev);

#endif

