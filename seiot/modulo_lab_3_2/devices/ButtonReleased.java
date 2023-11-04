package seiot.modulo_lab_3_2.devices;

import seiot.modulo_lab_3_2.common.*;

public class ButtonReleased implements Event {
	private Button source;
	
	public ButtonReleased(Button source){
		this.source = source;
	}
	
	public Button getSourceButton(){
		return source;
	}
}
