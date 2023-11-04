package seiot.modulo_lab_3_2.devices;

import seiot.modulo_lab_3_2.common.*;

public class MotionDetected implements Event {
	private ObservableMotionDetectorSensor source;
	
	public MotionDetected(ObservableMotionDetectorSensor source){
		this.source = source;
	}
	
	public ObservableMotionDetectorSensor getSource(){
		return source;
	}
}
