package org.example;

import jssc.*;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class SerialCommChannel implements CommChannel, SerialPortEventListener {
    private SerialPort serialPort;
    private BlockingQueue<String> queue;
    private StringBuffer currentMsg = new StringBuffer("");
    private static final char BT_MESSAGES_ID = 'B';
    private static final char _MESSAGES_ID = 'S';


    public SerialCommChannel(String port, int rate) throws Exception {
        queue = new ArrayBlockingQueue<>(100);
        serialPort = new SerialPort(port);
        try {
            serialPort.openPort();

            serialPort.setParams(rate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(this);

        } catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }
    }

        @Override
        public void serialEvent (SerialPortEvent serialPortEvent){
            /* if there are bytes received in the input buffer */
            if (serialPortEvent.isRXCHAR()) {
                try {
                    String msg = serialPort.readString(serialPortEvent.getEventValue());

                    msg = msg.replaceAll("\r", "");

                    currentMsg.append(msg);

                    boolean goAhead = true;

                    while(goAhead) {
                        String msg2 = currentMsg.toString();
                        int index = msg2.indexOf("\n");
                        if (index >= 0) {
                                queue.put(msg2.substring(0, index));
                                currentMsg = new StringBuffer("");
                            if (index + 1 < msg2.length()) {
                                currentMsg.append(msg2.substring(index + 1));
                            }
                        } else {
                            goAhead = false;
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }

        @Override
        public void sendMsg (String msg){
            char[] array = (msg+"\n").toCharArray();
            byte[] bytes = new byte[array.length];
            for (int i = 0; i < array.length; i++){
                bytes[i] = (byte) array[i]; //sta inviando anche il valore 10 dopo i valori base che dovrebbe
            }
            try {
                synchronized (serialPort) {
                    System.out.println("[SERIAL] | Invio: "+ (Arrays.toString(bytes)));
                    serialPort.writeBytes(bytes);
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public String receiveMsg () throws InterruptedException {
            return queue.take();
        }

        @Override
        public boolean isMsgAvaiable () {
            return !queue.isEmpty();
        }


}

