package handleMsg;

import common.Event;


public class MsgEvent implements Event {
	
	private String msg;
	
	public MsgEvent(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}
}
