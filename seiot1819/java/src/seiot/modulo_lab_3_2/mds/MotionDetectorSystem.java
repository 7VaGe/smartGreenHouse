package seiot.modulo_lab_3_2.mds;

import seiot.modulo_lab_3_2.devices.*;

public class MotionDetectorSystem {

	public static void main(String[] args) {
		
		AlarmCounter alarmCounter = new AlarmCounter();		
		Light led = new seiot.modulo_lab_3_2.devices.emu.Led(4);
		Button resetButton = new seiot.modulo_lab_3_2.devices.emu.Button(7);
		MotionDetectorSensor pir = new seiot.modulo_lab_3_2.devices.emu.PirSensor(2);
		Serial serialDev = new seiot.modulo_lab_3_2.devices.emu.SerialImpl(8, 9);
		
		MotionDetector mda = new MotionDetector(led, pir, resetButton, alarmCounter);
		InputMsgReceiver rec = new InputMsgReceiver(alarmCounter, serialDev);
		mda.start();
		rec.start();
	}

}
