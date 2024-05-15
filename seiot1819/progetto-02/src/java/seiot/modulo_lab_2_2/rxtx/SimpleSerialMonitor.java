package seiot.modulo_lab_2_2.rxtx;

/**
 * Test Serial Monitor, based on: 
 * http://playground.arduino.cc/Interfacing/Java
 *
 */
public class SimpleSerialMonitor  {

	public static void main(String[] args) throws Exception {
		if (args.length != 2){
			System.out.println("args: <CommPortName> <BoudRate>");
			System.exit(1);
		} else {
			String comPortName = args[0];
			int dataRate = Integer.parseInt(args[1]);
			System.out.println("Start monitoring serial port "+args[0]+" at boud rate: "+args[1]);
			try {
				SerialMonitor monitor = new SerialMonitor();
				monitor.start(comPortName,dataRate);							
				Thread.sleep(1000000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}