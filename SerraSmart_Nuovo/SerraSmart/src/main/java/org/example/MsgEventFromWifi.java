package org.example;



public class MsgEventFromWifi implements Event {

    private String msg;
    private char header;

    public MsgEventFromWifi(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }


    public char getHeader(String msg) {
        /*byte[] array = msg.getBytes();
        header = (char) array[0];*/
        return header =  msg.charAt(0);
    }
}
