package seiot.modulo_lab_4_3;

import seiot.modulo_lab_3_2.common.*;

public class MsgEvent implements Event {
	
	private String msg;
	
	public MsgEvent(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}
}
