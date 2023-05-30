package devices;

import common.Event;


public class Tick implements Event {
	
	private long time;
	/*
	 * Ogni task ha una funzione tick, che determina il periodo con cui il nostro task controlla il proprio stato.
	 * */
	public Tick(long time ){
		this.time = time;
	}
	
	public long getTime(){
		return time;
	}
}
