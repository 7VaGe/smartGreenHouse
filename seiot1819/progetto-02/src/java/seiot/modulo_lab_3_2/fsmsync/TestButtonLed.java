package seiot.modulo_lab_3_2.fsmsync;

import seiot.modulo_lab_3_2.devices.*;

public class TestButtonLed {
	public static void main(String[] args) {
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		Button button = new seiot.modulo_lab_3_2.devices.emu.Button(17);
		// Light led = new pse.modulo_lab_3_3.devices.dio_impl.Led(4);
		// Light led = new pse.modulo_lab_3_3.devices.p4j_impl.Led(4);
		// Button button = new pse.modulo_lab_3_3.devices.p4j_impl.Button(17);
		new ButtonLedController(button,led).start();
	}

}
