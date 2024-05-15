package seiot.modulo_lab_3_2.fsmsync;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.BasicFSMController;
import seiot.modulo_lab_3_2.devices.Button;
import seiot.modulo_lab_3_2.devices.Light;

public class ButtonLedController extends BasicFSMController {
	
	private Light led;
	private Button button;
	private enum State {ON,OFF}
	private State currentState;

	public ButtonLedController(Button button, Light led){
		super(50);
		this.led = led;
		this.button = button;
		currentState = State.OFF;
	}
	
	protected void tick(){
		switch (currentState){
		case ON:
			if (!button.isPressed()){
				try {
					led.switchOff();
					currentState = State.OFF;
				} catch (IOException ex){}
			}
			break;
		case OFF:
			if (button.isPressed()){
				try {
					led.switchOn();
					currentState = State.ON;
				} catch (IOException ex){}
			}
			break;
		}
	}
}
