package seiot.modulo_lab_4_3;

import seiot.modulo_lab_2_2.msg.CommChannel;

/**
 * Esempio di utilizzo del componente ExtendedSerialCommChannel 
 * che permette lo scambio di messaggi via seriale anche con 
 * emulatore Android (usato ad esempio per sostituire la comunicazione via 
 * Bluetooth).
 * 
 * 
 * @author acroatti
 *
 */
public class TestExtendedPingPong {

	public static void main(String[] args) throws Exception {
		// CommChannel channel = new SerialCommChannel(args[0],9600);	
		// CommChannel channel = new SerialCommChannel("/dev/cu.isi00-DevB",9600);	
		// CommChannel channel = new ExtendedSerialCommChannel("/dev/cu.usbmodem14201",9600);	
		CommChannel channel = new ExtendedSerialCommChannel("/dev/cu.isi00-DevB",9600);	
		
		/* attesa necessaria per fare in modo che Arduino completi il reboot */
		System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");		

		
		while (true){
			System.out.println("Sending ping");
			channel.sendMsg("ping");
			String msg = channel.receiveMsg();
			System.out.println("Received: "+msg);		
			Thread.sleep(500);
		}
	}

}
