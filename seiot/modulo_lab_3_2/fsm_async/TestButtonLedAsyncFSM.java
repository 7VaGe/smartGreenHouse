package seiot.modulo_lab_3_2.fsm_async;

import seiot.modulo_lab_3_2.devices.*;

public class TestButtonLedAsyncFSM {
	public static void main(String[] args) {
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		ObservableButton button = new seiot.modulo_lab_3_2.devices.emu.ObservableButton(17,"test");
		// Light led = new pse.modulo_lab_3_2.devices.dio_impl.Led(4);
		// Light led = new pse.modulo_lab_3_2.devices.p4j_impl.Led(4);
		// Button button = new pse.modulo_3_2.devices.p4j_impl.Button(17);
		
		new ButtonLedControllerAsyncFSM(button,led).start();
	}

}
