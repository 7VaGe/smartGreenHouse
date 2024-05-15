package seiot.modulo_lab_3_2.event;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class ButtonLedControllerWithHandlers extends EventLoopControllerWithHandlers {
	
	private Light led;
	private ObservableButton button;

	public ButtonLedControllerWithHandlers(ObservableButton button, Light led){
		super();
		this.led = led;
		this.button = button;
		button.addObserver(this);
	}
	
	@Override
	protected void setupHandlers() {
		addHandler(ButtonPressed.class, (Event ev) -> {
			try {
				led.switchOn();
			} catch (Exception ex) {}
		}).addHandler(ButtonReleased.class, (Event ev) -> {
			try {
				led.switchOff();
			} catch (Exception ex) {}
		});
	}
}
