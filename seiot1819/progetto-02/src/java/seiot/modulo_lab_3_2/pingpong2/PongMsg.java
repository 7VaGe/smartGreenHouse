package seiot.modulo_lab_3_2.pingpong2;

import seiot.modulo_lab_3_2.common.*;

public class PongMsg implements Msg {
	
	private int count;
	
	public PongMsg(int count){
		this.count = count;
	}
	
	public int getCount(){
		return count;
	}
	
}
