package org.example;

public class MsgEventFromSerial implements Event {

    private String msg;

    public MsgEventFromSerial(String msg){
        this.msg = msg;
    }
    /**
     * Getter for return the message from an MsgEventFromSerial
     *
     * @return message event
     * */
    public String getMsg(){
        return msg;
    }

    /**
     * Getter for return the message header from an MsgEventFromWifi
     *
     * @return header of message event
     * */
    public char getHeader(String msg) {
        return msg.charAt(0);
    }
}
