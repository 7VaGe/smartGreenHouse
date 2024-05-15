package seiot.modulo_lab_4_3;
import java.io.IOException;
import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class MyAgent extends BasicEventLoopController {
	
	private MsgService msgService;
	private ObservableTimer timer;

	private enum State { WAIT_FOR_PONG, WAIT_FOR_TICK };
	private State currentState;

	public MyAgent(String port, int rate){
		this.msgService = new MsgService(port, rate);		
		msgService.init();
		this.timer = new ObservableTimer();		
		timer.addObserver(this);
		msgService.addObserver(this);
		currentState = State.WAIT_FOR_PONG;
		msgService.sendMsg("ping");
	}
	
	protected void processEvent(Event ev){
		switch (currentState){
		case WAIT_FOR_PONG:
			try {
				if (ev instanceof MsgEvent){
					String msg = ((MsgEvent) ev).getMsg();
					System.out.println("Received: "+msg);		
					timer.start(500);
					currentState = State.WAIT_FOR_TICK;
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
			break;
		case WAIT_FOR_TICK:
			if (ev instanceof Tick){
				timer.stop();
				msgService.sendMsg("ping");
				currentState = State.WAIT_FOR_PONG;
			}
			break;
		}
	}
}
