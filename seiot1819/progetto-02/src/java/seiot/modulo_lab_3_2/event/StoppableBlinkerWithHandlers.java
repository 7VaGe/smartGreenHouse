package seiot.modulo_lab_3_2.event;

import java.io.IOException;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class StoppableBlinkerWithHandlers extends EventLoopControllerWithHandlers {
	
	private Light led;
	private ObservableButton stopButton;
	private ObservableTimer timer;
	
	private enum State {ON, OFF, IDLE};
	private State currentState;

	public StoppableBlinkerWithHandlers(ObservableButton button, Light led){
		this.led = led;
		this.stopButton = button;
		this.timer = new ObservableTimer();	
		this.startObserving(button);
		this.startObserving(timer);
		currentState = State.IDLE;
	}

	@Override
	protected void setupHandlers() {
		addHandler(ButtonPressed.class, (Event ev) -> {
			try {
				if (currentState == State.IDLE) {
					led.switchOn();
					timer.start(500);
					currentState = State.ON;
				} else if (currentState == State.ON) {
					led.switchOff();
					timer.stop();
					currentState = State.IDLE;
				}
			} catch (Exception ex) {}
		}).addHandler(Tick.class, (Event ev) -> {
			try {
				if (currentState == State.ON) {
					led.switchOff();
					currentState = State.OFF;
				} else {
					led.switchOn();
					currentState = State.ON;
				}
			} catch (Exception ex) {}
		});
	}
	
}
