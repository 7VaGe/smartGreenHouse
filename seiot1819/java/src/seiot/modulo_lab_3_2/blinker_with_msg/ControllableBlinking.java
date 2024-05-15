package seiot.modulo_lab_3_2.blinker_with_msg;

import seiot.modulo_lab_3_2.devices.*;

public class ControllableBlinking {
	public static void main(String[] args) {
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		Serial inputDev = new seiot.modulo_lab_3_2.devices.emu.SerialImpl(6, 7);
		
		Blinker blinker = new Blinker(led);
		InputMsgReceiver rec = new InputMsgReceiver(blinker,inputDev);
		blinker.start();
		rec.start();
	}

}
