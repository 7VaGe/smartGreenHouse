package seiot.modulo_lab_3_2.pingpong;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class Ponger extends ReactiveAgent {

	private Light myLed;
	private ReactiveAgent peer;
	
	public void init(Light led, ReactiveAgent peer){
		this.myLed = led;
		this.peer = peer;
	}
	
	@Override
	protected void processEvent(Event ev) {
		if (ev instanceof MsgEvent){
			Msg msg = ((MsgEvent) ev).getMsg();
			if (msg instanceof PingMsg){
				PingMsg pingMsg = ((PingMsg) msg);
				try {
					myLed.switchOn();
					waitFor(1000);
					myLed.switchOff();
					int count = pingMsg.getCount();
					sendMsgTo(peer, new PongMsg(count + 1));
					log("PONG!");
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	private void log(String msg){
		System.out.println("[PONGER] "+msg);
	}
	
	private void waitFor(long ms){
		try {
			Thread.sleep(ms);
		} catch (Exception ex){}
	}
}
