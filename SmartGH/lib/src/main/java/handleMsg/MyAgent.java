package handleMsg;
import java.util.Objects;
import java.util.Optional;
import com.google.common.primitives.Ints;

import common.Event;
import common.EventLoopController;
import devices.Tick;


public class MyAgent extends EventLoopController {
	
	private MsgService msgService;
	//private ObservableTimer timer;

	private enum State { MSG_HANDLER, WAIT_FOR_TICK, DISTRIBUTION };
	private State currentState;
	private static final double DELTA= 0.05;
	private static final double UMIN = 0.10;
	private static final double UMED = 0.20;
	private static final double UMAX = 0.30;
	private static final int DEFAULT_DISTRIBUTION = 15;
	//private static final int MAX_DISTRIBUTION = 15;
	private String oldSnd;

	public MyAgent(String port, int rate){
		this.msgService = new MsgService(port, rate);		
		msgService.init();
		//this.timer = new ObservableTimer(null, null, null) controlla come funziona questo modulo
		//timer.addObserver(this);
		msgService.addObserver(this);
		currentState = State.MSG_HANDLER;

	}
	protected void processEvent(Event ev){
		switch (currentState){
		case MSG_HANDLER:
			try {
				if (ev instanceof MsgEvent){
					String msg = ((MsgEvent) ev).getMsg();
					String sendMessage="";
					System.out.println("Ricevuto: "+msg);
					if(msg.startsWith("B")) { 
						System.out.print("Bluetooth | ");
						String prefix = msg.substring(0, 2);
						switch (prefix) { //BWO
							case "BWO" : System.out.println("Comando di apertura pompa");
								String df = Optional.ofNullable(DEFAULT_DISTRIBUTION)  //puoi usare anche Integer.toString(DEFAULT_DISTRIBUTION);
						        .map(Objects::toString)
						        .orElse("Errore | Default convertion"); //Conversione intero a stringa. 
								checkAndSendChannel(sendMessage,df); 
								currentState = State.DISTRIBUTION;
								break;
							case "BWC" : System.out.println("Comando di chiusura pompa");
								checkAndSendChannel(sendMessage, "BWC");
								break;
							default : 	String waterValue = msg.substring(1, 2); //B12
										int val = Optional.ofNullable(waterValue)
												.map(Ints::tryParse)
												.orElse(-1);
										System.out.println("Il valore che hai ricevuto in litri al minuto è:" + val);
										checkAndSendChannel(sendMessage, waterValue);
										currentState = State.DISTRIBUTION;
								break;
						}
					}else if(msg.startsWith("U")) {
						System.out.println("dato ricevuto da ESP32");
						String umidityValue = msg.substring(1,3);//U100 o U001 U089
						int val = Optional.ofNullable(umidityValue)
								.map(Ints::tryParse)
								.orElse(-1);
						float umidityValuePercentage =  (val/100);
						checkAndSendChannel(sendMessage, msg);
						
						if(umidityValuePercentage < UMIN) {
							System.out.println("EROGA | PMAX");
							checkAndSendChannel(sendMessage, "1");
							
							currentState = State.DISTRIBUTION;
							
						}else if (umidityValuePercentage > UMIN && umidityValuePercentage < UMED) {
							System.out.println("EROGA | PMED");
							checkAndSendChannel(sendMessage, "2");
							currentState = State.DISTRIBUTION;
							
						}else if(umidityValuePercentage > UMED && (umidityValuePercentage < (UMAX + DELTA ))){
							System.out.println("EROGA | PMIN");
							checkAndSendChannel(sendMessage, "3");
							currentState = State.DISTRIBUTION;
							
						}else if ( umidityValuePercentage > (UMAX + DELTA )){
							checkAndSendChannel(sendMessage, "4"); //chiudi pompa.
						}
					}
					//timer.start(500); //controllare
					currentState = State.WAIT_FOR_TICK;
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
			break;
		case WAIT_FOR_TICK:
			if (ev instanceof Tick){
				currentState = State.MSG_HANDLER;
			}
			break;
		case DISTRIBUTION:
			if (ev instanceof MsgEvent){
				currentState = State.MSG_HANDLER;
				}
				//StopTimer st =  new StopTimer(MAX_DISTRIBUTION);
				
			break;
	}	
}
			//imposta il timer a 5 secondi, poi manda il comando di chiusura pompa e torna nello stato di attesa messaggio.
			
	
	public void checkAndSendChannel(String sendMessage, String msg) throws InterruptedException {
		if(sendMessage == null || sendMessage == "") {
			sendMessage = msg;
			msgService.sendMsg(sendMessage);
		}else{
			System.out.println("SendMessage contiene un messaggio: " + sendMessage + "lo salvo in oldSnd" + oldSnd);
			oldSnd = sendMessage;
			sendMessage = msg;
			msgService.sendMsg(sendMessage);
		}		
}
}
