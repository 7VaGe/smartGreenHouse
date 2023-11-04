package seiot.modulo_lab_3_2.devices;

import seiot.modulo_lab_3_2.common.*;

public class PotValueChanged implements Event {
	private ObservablePotentiometer source;
	private int value;
	
	public PotValueChanged(ObservablePotentiometer source, int value){
		this.source = source;
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public ObservablePotentiometer getSource(){
		return source;
	}
}
