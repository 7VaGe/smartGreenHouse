package seiot.modulo_lab_3_2.super_loop;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.SuperloopController;
import seiot.modulo_lab_3_2.devices.Button;
import seiot.modulo_lab_3_2.devices.Light;

public class ButtonLedController extends SuperloopController {
	
	private Light led;
	private Button button;
	
	public ButtonLedController(Button button, Light led){
		this.led = led;
		this.button = button;
	}

	protected void setup() {
	}
	
	protected void loop(){
		try {
			if (button.isPressed()){
				led.switchOn();
			} else {
				led.switchOff(); 
			}
			waitFor(50);
		} catch (InterruptedException ex){
			ex.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
