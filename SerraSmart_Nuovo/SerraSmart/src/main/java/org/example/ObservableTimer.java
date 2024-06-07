package org.example;

import java.util.concurrent.*;

public class ObservableTimer extends Observable {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> tickHandle;
    public volatile boolean isRunning = true;
    public ObservableTimer() {
    }
    /**
     * Start generating tick event
     *
     * @param period period in milliseconds
     */
    public synchronized void start(long period) {
        if (tickHandle == null || tickHandle.isCancelled() || tickHandle.isDone()) {
            isRunning = true;
            tickHandle = null;
            scheduleTick(period);
        }
    }
    /**
     * Generate a tick event after a number of milliseconds
     *
     * @param deltat
     */
    public synchronized void scheduleTick(long deltat) {
        tickHandle = scheduler.schedule(() -> {
            if (isRunning) {
                Tick ev = new Tick(System.currentTimeMillis());
                notifyEvent(ev);
            }
        }, deltat, TimeUnit.MILLISECONDS);
    }
    /**
     * Stop generating tick event
     */
    public synchronized void stop() {
        if (tickHandle != null && !tickHandle.isCancelled()) {
            tickHandle.cancel(true);
            tickHandle = null;
        }
    }

}

