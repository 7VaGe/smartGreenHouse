package timer;

import java.util.Timer;
import java.util.TimerTask;

import common.Event;


public class TimerEvent extends TimerTask implements Event  {
	
	private Thread thread;
    private Timer timer;

    public void TimeOutTask(Thread thread, Timer timer) {
        this.thread = thread;
        this.timer = timer;
    }
    
	@Override
	public void run() {
		 if(thread != null && thread.isAlive()) {
	            thread.interrupt();
	            timer.cancel();
	     }
 }
}
