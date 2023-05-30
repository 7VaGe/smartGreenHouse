package channel;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialWriter implements Runnable {
    private SerialPort serialPort;
    private String message;

    public SerialWriter(SerialPort serialPort, String message) {
        this.serialPort = serialPort;
        this.message = message;
    }

    public void run() {
        try {
            serialPort.writeBytes(message.getBytes());
        } catch (SerialPortException ex) {
            System.out.println("Error writing to serial port: " + ex.getMessage());
        }
    }
}
