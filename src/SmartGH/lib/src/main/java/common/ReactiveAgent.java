package common;

public abstract class ReactiveAgent extends EventLoopController {
	
	protected ReactiveAgent(int size){
		super(size);
	}

	protected ReactiveAgent(){
	}
	
	protected boolean sendMsgTo(ReactiveAgent agent, Msg m){
		MsgEvent ev = new MsgEvent(m,this);
		return agent.notifyEvent(ev);
	}
}
