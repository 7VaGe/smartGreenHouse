package seiot.modulo_lab_4_3;
import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class MyAgent extends EventLoopController {
	
	private MsgService msgService;
	private ObservableTimer timer;

	private enum State { WAIT_FOR_MSG, WAIT_FOR_TICK };
	private State currentState;
/*
 *	Non sono sicuro se devo inserire qui le differenze dei messaggi ricevuti, ho un gran dubbio,
 *	dato che qui gestisco anche i passaggi di stato, forse converrebbe lasciare inalterato il msgService,
 *	e richiamarne i suoi metodi qui dentro. potrebbe essere più adatto.
 */
	public MyAgent(String port, int rate){
		this.msgService = new MsgService(port, rate);		
		msgService.init();
		this.timer = new ObservableTimer();		
		timer.addObserver(this);
		msgService.addObserver(this);
		currentState = State.WAIT_FOR_MSG;
		//msgService.sendMsg("ping");
	}
	
	protected void processEvent(Event ev){
		switch (currentState){
		case WAIT_FOR_MSG:
			try {
				if (ev instanceof MsgEvent){
					String msg = ((MsgEvent) ev).getMsg();
					System.out.println("Receivuto: "+msg);		
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
				//msgService.sendMsg("ping");
				currentState = State.WAIT_FOR_MSG;
			}
			break;
		}
	}
}
