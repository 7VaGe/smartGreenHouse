package org.example;

public class Tick implements Event {

    private long time;
    /**
     * Tick object used on pattern observer to notify a timer event, the tick corresponds to the elapsed time of
     * X millisecond estabilished by the application request
     *
     * @param time the Tick Time
     * */
    public Tick(long time ){
        this.time = time;
    }
    /**
     * Getters for the Tick time
     *
     * @return the current Tick time.
     * */
    public long getTime(){
        return time;
    }
}
