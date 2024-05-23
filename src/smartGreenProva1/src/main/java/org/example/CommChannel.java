package org.example;

public interface CommChannel {
    void sendMsg(String msg);

    String receiveMsg() throws InterruptedException;

    boolean isMsgAvaiable();


}