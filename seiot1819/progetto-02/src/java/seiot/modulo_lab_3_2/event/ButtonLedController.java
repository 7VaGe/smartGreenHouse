package seiot.modulo_lab_3_2.event;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class ButtonLedController extends BasicEventLoopController {
	
	private Light led;
	private ObservableButton button;

	public ButtonLedController(ObservableButton button, Light led){
		this.led = led;
		this.button = button;
		button.addObserver(this);
	}
	
	protected void processEvent(Event ev){
		try {
			if (ev instanceof ButtonPressed){
				led.switchOn();
			} else if (ev instanceof ButtonReleased){
				led.switchOff();
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
