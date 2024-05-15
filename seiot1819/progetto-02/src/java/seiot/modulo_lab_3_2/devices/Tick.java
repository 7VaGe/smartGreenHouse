package seiot.modulo_lab_3_2.devices;

import seiot.modulo_lab_3_2.common.*;

public class Tick implements Event {
	
	private long time;
	
	public Tick(long time ){
		this.time = time;
	}
	
	public long getTime(){
		return time;
	}
}
