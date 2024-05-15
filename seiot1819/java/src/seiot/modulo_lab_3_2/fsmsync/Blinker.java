package seiot.modulo_lab_3_2.fsmsync;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.BasicFSMController;
import seiot.modulo_lab_3_2.devices.Light;

public class Blinker extends BasicFSMController {
	
	private Light led;
	private enum State {ON,OFF}
	private State currentState;
	
	public Blinker(Light led){
		super(100);
		this.led = led;
		currentState = State.OFF;
	}
	
	protected void tick(){
		switch (currentState){
		case ON:
			try {
				led.switchOff();
				currentState = State.OFF;
			} catch (IOException ex){}
			break;
		case OFF:
			try {
				led.switchOn();
				currentState = State.ON;
			} catch (IOException ex){}
			break;
		}
	}
}
