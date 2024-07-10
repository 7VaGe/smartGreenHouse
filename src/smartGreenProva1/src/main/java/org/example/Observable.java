package org.example;

import java.util.LinkedList;

public class Observable {

    private final LinkedList<Observer> observers;

    protected Observable(){
        observers = new LinkedList<>();
    }
    /**
     * Notify the event foreach observers inside the LinkedList
     *
     * @param ev event
     * */
    protected void notifyEvent(Event ev){
        synchronized (observers){
            for (Observer obs: observers){
                obs.notifyEvent(ev);
            }
        }
    }
    /**
     * Add the observer from LinkedList
     *
     * @param obs observer to add
     * */
    public void addObserver(Observer obs){
        synchronized (observers){
            observers.add(obs);
        }
    }
    /**
     * Remove the observer from LinkedList
     *
     * @param obs observer to remove
     * */
    public void removeObserver(Observer obs){
        synchronized (observers){
            observers.remove(obs);
        }
    }

}