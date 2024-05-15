package seiot.modulo_lab_2_2.msg;

import seiot.modulo_lab_2_2.msg.rxtx.SerialCommChannel;

/**
 * Testing simple message passing.
 * 
 * To be used with an Arduino connected via Serial Port
 * running modulo-2.3/remote_blinking_withstates.ino
 * 
 * @author aricci
 *
 */
public class TestRemoteBlinkingWithStates {

	public static void main(String[] args) throws Exception {
		SerialCommChannel channel = new SerialCommChannel(args[0],9600);		
		/* attesa necessaria per fare in modo che Arduino completi il reboot */
		System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");		

		Thread.sleep(3000);
		channel.sendMsg("start");
		String msg = channel.receiveMsg();
		if (msg.equals("started")){
			System.out.println("OK, stop blinking in 5 secs...");
			Thread.sleep(5000);
			channel.sendMsg("stop");
			msg = channel.receiveMsg();
			if (msg.equals("stopped")){
				System.out.println("OK, stopped.");
			} else {
				System.out.println("Something wrong with the protocol - received "+msg);
			}
		} else {
			System.out.println("Something wrong with the protocol - received "+msg);
		}
		
		System.exit(0);
	}

}
