package seiot.modulo_lab_3_2.devices;

import seiot.modulo_lab_3_2.common.*;

public class ButtonPressed implements Event {
	private Button source;
	
	public ButtonPressed(Button source){
		this.source = source;
	}
	
	public Button getSourceButton(){
		return source;
	}
}
