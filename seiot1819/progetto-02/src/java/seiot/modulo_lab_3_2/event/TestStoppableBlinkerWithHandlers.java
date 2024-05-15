package seiot.modulo_lab_3_2.event;

import seiot.modulo_lab_3_2.devices.*;

public class TestStoppableBlinkerWithHandlers {
	public static void main(String[] args) {
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		ObservableButton button = new seiot.modulo_lab_3_2.devices.emu.ObservableButton(17,"test");
		new StoppableBlinkerWithHandlers(button,led).start();
	}

}
