package seiot.modulo_lab_4_3;

import seiot.modulo_lab_2_2.msg.CommChannel;

/**
 * 
 * Installazione msg bridge - Seriale <--> IP
 * per comunicazione con emulatore Android 
 * 
 * @author aricci
 *
 */
public class LaunchMsgBridge {

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.err.println("Params: SerialPortName IPport");
		}
		
		/* setting up the channel, with server for the emu*/
		System.out.print("Installing the bridge...");		
		CommChannel channel = new ExtendedSerialCommChannel(args[0], 9600, Integer.parseInt(args[1]));	
		System.out.println("Ready");		
	}

}
