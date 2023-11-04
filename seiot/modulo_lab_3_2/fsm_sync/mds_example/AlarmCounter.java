package seiot.modulo_lab_3_2.fsm_sync.mds_example;

public class AlarmCounter {

	private int count;
	
	public AlarmCounter(){
		count = 0;
	}
	
	public synchronized void incNumAlarms(){
		count++;
	}
	
	public synchronized int getCurrentNumAlarms(){
		return count;
	}
}
