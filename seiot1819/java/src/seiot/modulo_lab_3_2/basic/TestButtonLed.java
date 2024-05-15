package seiot.modulo_lab_3_2.basic;

import seiot.modulo_lab_3_2.devices.*;

public class TestButtonLed {
	public static void main(String[] args) {
	    Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		Button button = new seiot.modulo_lab_3_2.devices.emu.Button(17);
		// Light led = new seiot.modulo_lab_3_2.dio_impl.Led(4);
		// Light led = new seiot.modulo_lab_3_2.devices.p4j_impl.Led(7);
		// Button button = new seiot.modulo_lab_3_2.devices.p4j_impl.Button(4);
		new ButtonLedController(button,led).start();
	}

}
