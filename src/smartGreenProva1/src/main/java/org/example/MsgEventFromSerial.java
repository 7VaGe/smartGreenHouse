package org.example;



public class MsgEventFromSerial implements Event {

    private String msg;
    private char header;

    public MsgEventFromSerial(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }

    //qui puoi inserire il fatto degli header se vuoi cambiare stato con un getHeader passando il messaggionper intero.
    public char getHeader(String msg) {
        /*byte[] array = msg.getBytes();
        header = (char) array[0];*/
        return header =  msg.charAt(0);
    }
}
