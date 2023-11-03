package handleMsg;

import channel.SerialCommChannel;

public class MsgService extends common.Observable {

	private ExtendedSerialCommChannel channel;
	private String port;
	private int rate;
	
	
	public MsgService(String port, int rate){
		this.port = port;
		this.rate = rate;
	}
	
	public void init(){
		try {
			channel = new ExtendedSerialCommChannel(port, rate);
			//channel = new SerialCommChannel(port, rate);	
			System.out.println("Waiting Arduino for rebooting...");		
			Thread.sleep(4000); //massimo per vertx 2000ms ora aggiungo un altro thread nella parte prima di inviare i msg
			System.out.println("Ready.");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new Thread(() -> {
			while (true) {
				try { //COM4 risulta essere sempre occupata
					if(channel.receiveMsg()!= null) {
					String msg = channel.receiveMsg();
					this.notifyEvent(new MsgEvent(msg));
					
					}				
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void sendMsg(String msg) {
		try {
			channel.close();
			synchronized (channel) {
				channel = new ExtendedSerialCommChannel(port, rate);
				channel.sendMsg(msg);
				System.out.println("sent "+msg);
				//close
				
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	
	}
	
}

