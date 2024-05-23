package org.example;

import java.util.concurrent.*;
import java.util.TimerTask;
import java.util.Timer;

public class ObservableTimer extends Observable  {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> tickHandle;
    private Runnable tickTask;

    public ObservableTimer(){

        tickTask = () -> {
            Tick ev = new Tick(System.currentTimeMillis());
            notifyEvent(ev);
        };
    }
    /**
     * Start generating tick event
     *
     * @param period period in milliseconds
     */
    public synchronized void start(long period){
        if(tickHandle == null || tickHandle.isCancelled() || tickHandle.isDone()){
            tickHandle = scheduler.scheduleAtFixedRate(tickTask, 0, period, TimeUnit.MILLISECONDS);
            System.out.println("[ObservableTimer] | Timer partito: "+ period);
        }

    }

    /**
     * Generate a tick event after a number of milliseconds
     *
     * @param deltat
     */
    public synchronized void scheduleTick(long deltat){
        scheduler.schedule(tickTask, deltat, TimeUnit.MILLISECONDS);
        System.out.println("[ObservableTimer] | Timer partito! Tick schedulato dopo: "+ deltat + "ms");
    }
    /**
     * Stop generating tick event
     *
     */
    public synchronized void stop(){
        if (tickHandle != null && !tickHandle.isCancelled()){
            tickHandle.cancel(false);
            System.out.println("[ObservableTimer] | Timer Stop!");
            tickHandle = null;
        }
    }

}
