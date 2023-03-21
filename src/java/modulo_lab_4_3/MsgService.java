package seiot.modulo_lab_4_3;

import java.util.Optional;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;
import com.google.common.primitives.Ints;
import seiot.modulo_lab_2_2.msg.CommChannel;

public class MsgService extends seiot.modulo_lab_3_2.common.Observable {

	private CommChannel channel;
	private String port;
	private int rate;
	private static final double DELTA= 0.05;
	private static final double UMIN = 0.10;
	private static final double UMED = 0.20;
	private static final double UMAX = 0.30;
	private String oldSnd;
	
	public MsgService(String port, int rate){
		this.port = port;
		this.rate = rate;
	}
	
	void init(){
		try {
			channel = new ExtendedSerialCommChannel(port, rate);	
			// channel = new SerialCommChannel(port, rate);	
			System.out.println("Waiting Arduino for rebooting...");		
			Thread.sleep(4000);
			System.out.println("Ready.");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new Thread(() -> {
			while (true) {
				try {
					String msg = channel.receiveMsg();
					String sendMessage="";
					System.out.println("received: "+msg);
					if(msg.startsWith("B")) {
						System.out.println("dato ricervuto dal Bluetooth");
						String prefix = msg.substring(0, 2);
						switch (prefix) { //BWO12
							case "BWO" : System.out.println("Comando di apertura pompa");
								String liters = msg.substring(3,4);									
								if(liters != null) {
									checkAndSendChannel(sendMessage, msg); 
									//BWO12 e deve spacchettarlo prima apre la pompa e poi la imposta a 12
								}
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
										/*
										 * ho cercato di creare una classe con i generici, per poter inserire il water value
										 * effettivo messaggio che invia, come numero inrtero nel channel seriale, ma non 
										 * sono riuscito ad eseguire, i generici mi fanno creare una classe, e dovrei cambiare proprio
										 * la tipologia della classe messageservice, usando i generici e rifare praticamente tutto
										 * Se ho tempo eseguiro il cambiamento confidando che i metodi funzionino allo stesso modo.
										 */
								break;
						}
					}else if(msg.startsWith("U")) {
						System.out.println("dato ricevuto da ESP32");
						String umidityValue = msg.substring(1,3);//U100 o U001 U089
						int val = Optional.ofNullable(umidityValue)
								.map(Ints::tryParse)
								.orElse(-1);
						float umidityValuePercentage =  (val/100);
						if(umidityValuePercentage < UMIN) {
							/*
							 * devo valutare quale messaggio mandare in risposta dal server java ad arduino.
							 * A livello di logica non è il server a mandare i dati ad arduino, bensì
							 * arduino è il mio client, e dovrebbe richiedere i dati dal server con un periodo
							 * ben strutturato.
							 * ESP32 invia i dati ogni 2 secondi al server JAVA
							 * ARDUINO Richiede i dati al server ogni 2 secondi.
							 * SERVER elabora i dati ricevuti e aggiorna il valore delle variabili pronte per essere
							 * inviate ad arduino non appena riceve una richiesta.
							 * 
							 * Valutare un prefisso ulteriore nel caso avvengano richieste da parte del server, oppure gestire 
							 * una catena de'eventi che notificano ai miei dispositivi il cambio di dati, mi viene in mente 
							 * uno dei protocolli di propagazione delle tabelle di routing, ma non so se si possa fare.
							 */
							System.out.println("Portata massima: \n"
									+ "- Invia messaggio ad arduino per il servo ad ampiezza massima. \n"
									+ "- Invia messaggio ad arduino per accendere il led2 ad intensità pari a 100");
							checkAndSendChannel(sendMessage, "01");
						}else if (umidityValuePercentage > UMIN && umidityValuePercentage < UMED) {
							//devo valutare quale messaggio mandare da java ad arduino.
							System.out.println("Portata media: \n"
									+ "- Invia messaggio ad arduino per il servo ad ampiezza media. \n"
									+ "- Invia messaggio ad arduino per accendere il led2 ad intensità pari a 66");
							checkAndSendChannel(sendMessage, "02");
						}else if(umidityValuePercentage > UMED && (umidityValuePercentage < (UMAX + DELTA ))){
							//devo valutare quale messaggio mandare da java ad arduino.
							System.out.println("Portata minima: \n"
									+ "- Invia messaggio ad arduino per il servo ad ampiezza minima. \n"
									+ "- Invia messaggio ad arduino per accendere il led2 ad intensità pari a 33");
							checkAndSendChannel(sendMessage, "03");
						}else if ( umidityValuePercentage > (UMAX + DELTA )){
							checkAndSendChannel(sendMessage, "04"); //chiudi pompa.
						}
					}
					this.notifyEvent(new MsgEvent(msg));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	public void sendMsg(String msg) {
		channel.sendMsg(msg);
		System.out.println("sent "+msg);
	}
	
	public void checkAndSendChannel(String sendMessage, String msg) {
		if(sendMessage == null || sendMessage == "") {
			sendMessage = msg;
			channel.sendMsg(sendMessage);
		}else{
			System.out.println("SendMessage contiene un messaggio: " + sendMessage + "lo salvo in oldSnd");
			oldSnd = sendMessage;
			sendMessage = msg;
			channel.sendMsg(sendMessage);
		}		
}
}

