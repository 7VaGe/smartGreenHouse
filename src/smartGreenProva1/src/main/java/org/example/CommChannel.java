package org.example;

public interface CommChannel {
    /**
     * Send the message through Serial Channel
     *
     * @param msg the message to send to Serial Channel
     * */
    void sendMsg(String msg);
    /**
     * Receive the message through Serial Channel
     *
     * */
    String receiveMsg() throws InterruptedException;
    /**
     * Checking flag used to monitoring the Serial Channel
     *
     * */
    boolean isMsgAvaiable();


}