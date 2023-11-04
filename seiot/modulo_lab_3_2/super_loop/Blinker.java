package seiot.modulo_lab_3_2.super_loop;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.SuperloopController;
import seiot.modulo_lab_3_2.devices.Light;

public class Blinker extends SuperloopController {
	
	private Light led;
	private int period;
	
	public Blinker(Light led, int period){
		this.led = led;
		this.period = period;
	}
	
	protected void setup() {
	}
	
	protected void loop(){
		try {
			led.switchOn();
			waitFor(period);
			led.switchOff(); 
			waitFor(period);
		} catch (InterruptedException ex){
			ex.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
