package org.example;

public class MsgService extends Observable{
    private SerialCommChannel channel;
    private String port;
    private int rate;


    public MsgService(String port, int rate) {
        this.port = port;
        this.rate = rate;
    }

    /**
     * Initialize the channel passing the right port and baud rate
     *
     */
    void init(){
        try {
            channel = new SerialCommChannel(port, rate);
            System.out.println("Waiting Arduino for rebooting...");
            Thread.sleep(4000);
            System.out.println("Ready.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while (true) {
                try {
                    String msg = channel.receiveMsg();
                     System.out.println("[MSG SERVICE] - Received "+ msg);
                    this.notifyEvent(new MsgEventFromSerial(msg));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * Send message on a created channel
     *
     * @param msg message to send
     * */

    public void sendMsg(String msg) {
        channel.sendMsg(msg);
    }

}

