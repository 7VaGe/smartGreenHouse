package seiot.modulo_lab_3_2.common;

public abstract class SuperloopController extends Thread {

	protected void waitFor(long ms) throws InterruptedException{
		Thread.sleep(ms);
	}
	
	abstract protected void setup();

	abstract protected void loop();
	
	public void run() {
		setup();
		while (true) {
			loop();
		}
	}
	
}
