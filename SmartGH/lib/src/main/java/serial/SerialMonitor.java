package serial;

import jssc.*;

public class SerialMonitor implements SerialPortEventListener {
	SerialPort serialPort;
	
	public void start(String portName) {
		serialPort = new SerialPort(portName);
		try {
		    serialPort.openPort();

		    serialPort.setParams(SerialPort.BAUDRATE_9600,
		                         SerialPort.DATABITS_8,
		                         SerialPort.STOPBITS_1,
		                         SerialPort.PARITY_NONE);

		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);

		    serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
		}
		catch (SerialPortException ex) {
		    System.err.println("There are an error on writing string to port - " + ex);
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		try {
			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.closePort();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	@Override
	public synchronized void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                System.out.print(receivedData);
            }
            catch (SerialPortException ex) {
                System.err.println("Error in receiving string from COM-port: " + ex);
            }
        }
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2){
			System.err.println("args: <CommPortName> <boud rate>");
			System.exit(1);
		} else {
			try {
				String comPortName = args[0];
				int boudRate = Integer.parseInt(args[1]);
				 System.out.println("Start monitoring serial port " + args[0]+" at "+ boudRate +" boud rate");
				try {
					SerialMonitor monitor = new SerialMonitor();
					monitor.start(comPortName);							
					Thread.sleep(0); /* for ever */
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} catch (Exception ex) {
			}
		}
	}	
}