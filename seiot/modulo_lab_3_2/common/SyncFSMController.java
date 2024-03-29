package seiot.modulo_lab_3_2.common;

public abstract class SyncFSMController extends Thread {
	
	private int period;
	
	protected SyncFSMController(int period){
		this.period = period;
	}
	
	protected int getPeriod(){
		return period;
	}
	
	abstract protected void tick();
	
	public void run(){
		while (true){
			long t0 = System.currentTimeMillis();
			tick();
			long t1 = System.currentTimeMillis();
			long deltat = period - (t1-t0);
			if (deltat > 0){
				try {
					Thread.sleep(deltat);
				} catch (Exception ex){
					ex.printStackTrace();
				}
			} else {
				System.err.println("Task "+this.getClass().getSimpleName()+" overrun");
			}
		}
	}
	protected void waitFor(long ms) throws InterruptedException{
		Thread.sleep(ms);
	}
}
