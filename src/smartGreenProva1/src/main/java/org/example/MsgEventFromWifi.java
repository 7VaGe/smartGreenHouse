package org.example;



public class MsgEventFromWifi implements Event {

    private String msg;
    private char header;

    public MsgEventFromWifi(String msg){
        this.msg = msg;
    }
    /**
     * Getter for return the message from an MsgEventFromWifi
     *
     * @return message event
     * */
    public String getMsg(){
        return msg;
    }

    /**
     * Getter for return the header of an MsgEventFromWifi
     *
     * @return header of message event
     * */
    public char getHeader(String msg) {
        return header =  msg.charAt(0);
    }
}
