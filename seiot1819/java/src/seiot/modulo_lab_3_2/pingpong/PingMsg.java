package seiot.modulo_lab_3_2.pingpong;

import seiot.modulo_lab_3_2.common.*;

public class PingMsg implements Msg {
	
	private int count;
	
	public PingMsg(int count){
		this.count = count;
	}
	
	public int getCount(){
		return count;
	}
	
}
