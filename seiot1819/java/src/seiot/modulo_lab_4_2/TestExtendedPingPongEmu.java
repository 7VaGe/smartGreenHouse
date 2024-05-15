package seiot.modulo_lab_4_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
public class TestExtendedPingPongEmu {

	public static void main(String[] args) throws Exception {

		String serialPortName = "/dev/cu.usbmodem14201";
		
		/* setting up the channel, with server for the emu*/
		System.out.print("Creating the serial comm channel with IP server ...");		
		CommChannel channel = new ExtendedSerialCommChannel(serialPortName, 9600, 8080);	
		System.out.println("OK");		
		

		System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");		
		
		Socket socket = new Socket("localhost",8080);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));			
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		  
		while (true) {
			System.out.println("Sending ping on BT emu channel...");		
			out.write("ping$\n");
			out.flush();
			
			String msg = in.readLine();
			System.out.println("Received: " + msg);		
			Thread.sleep(500);

			System.out.println("Sending ping on the serial channel...");
			channel.sendMsg("ping");
			msg = channel.receiveMsg();
			System.out.println("Received: "+msg);		
			Thread.sleep(500);
		
		}
	}

}
