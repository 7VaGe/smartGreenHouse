package seiot.modulo_lab_3_2.basic;

import seiot.modulo_lab_3_2.devices.Light;

public class TestBlinking {
	public static void main(String[] args) {
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		// Light led = new seiot.modulo_lab_3_3.devices.dio_impl.Led(4);
		// Light led = new seiot.modulo_lab_3_3.devices.p4j_impl.Led(7);
		new Blinker(led,500).start();
		// Light led2 = new seiot.modulo_lab_3_3.devices.emu.Led(2,"led2");
		// new Blinker(led2,250).start();
	}

}
