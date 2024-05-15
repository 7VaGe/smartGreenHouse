package seiot.modulo_lab_3_2.devices;

import java.util.concurrent.*;

import seiot.modulo_lab_3_2.common.Observable;

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
		tickHandle = scheduler.scheduleAtFixedRate(tickTask, 0, period, TimeUnit.MILLISECONDS);	    
	}

	/**
	 * Generate a tick event after a number of milliseconds
	 * 
	 * @param delta
	 */
	public synchronized void scheduleTick(long deltat){
		scheduler.schedule(tickTask, deltat, TimeUnit.MILLISECONDS);
	}
	/**
	 * Stop generating tick event
	 * 
	 * @param period period in milliseconds
	 */
	public synchronized void stop(){
		if (tickHandle != null){
			tickHandle.cancel(false);
			tickHandle = null;
		}
	}
	
}
