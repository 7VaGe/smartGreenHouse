package seiot.modulo_lab_3_2.pingpong2;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class Pinger extends ReactiveAgent {

	private Light myLed;
	private ReactiveAgent peer;
	private ObservableTimer timer;
	private int currentCount;
	
	public void init(Light led, ReactiveAgent peer){
		this.myLed = led;
		this.peer = peer;
		this.timer = new ObservableTimer();
		timer.addObserver(this);
	}
	
	@Override
	protected void processEvent(Event ev) {
		if (ev instanceof MsgEvent){
			Msg msg = ((MsgEvent) ev).getMsg();
			if (msg instanceof BootMsg){
				sendMsgTo(peer, new PingMsg(0));
			} else if (msg instanceof PongMsg){
				PongMsg pongMsg = ((PongMsg) msg);
				currentCount = pongMsg.getCount();
				try {
					myLed.switchOn();
					timer.scheduleTick(1000);
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		} else if (ev instanceof Tick){
			try {
				myLed.switchOff();
				sendMsgTo(peer, new PingMsg(currentCount + 1));
				log("PING!");
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	private void log(String msg){
		System.out.println("[PINGER] "+msg);
	}
}
