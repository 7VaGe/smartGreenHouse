package org.example;


import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.Objects;


public class GreenHouseAgent extends BasicEventLoopController {
    private final MsgService msgService;
    private final EventBus eventBus;

    @Override
    public boolean notifyEvent(Event ev) {
        processEvent(ev);
        return true;
    }

    private enum State {MANUAL, AUTOMATIC}
    private State currentState;
    private static final double DELTA = 0.05;
    private static final double UMIN= 0.30;
    private static final double UMED= 0.20;
    private static final double UMAX= 0.10;
    private static final char PMIN= 'c';
    private static final char PMED= 'd';
    private static final char PMAX= 'e';
    private static final char PCLOSE= 'f';
    private static final String BLUETOOTHCHANGESTATEMANUAL= "m";
    private static final String BLUETOOTHCHANGESTATEAUTO= "a";
   // private static final double ZERO = 0.00;
    private String oldMsgWitholdValue = "";
    private final ObservableTimer timer;
    private boolean timerIsAlive = false;
    private boolean pumpState = false;
    private double msgFromWifiEventBridge;

    public GreenHouseAgent(String port, int rate, EventBus eventBus){
        super();
        this.msgService = new MsgService(port, rate);
        msgService.init();
        msgService.addObserver(this);
        this.timer = new ObservableTimer();
        timer.addObserver(this);
        currentState = State.AUTOMATIC;
        this.eventBus = eventBus;

        eventBus.consumer("data.new", msg -> {
            JsonObject event = (JsonObject) msg.body();
            double value = event.getDouble("value");
            processEvent(new MsgEventFromWifi(Double.toString(value)));
        });
    }
    public void sendData(String data){
        msgService.sendMsg(data);
    }

    @Override
    protected void processEvent(Event ev) {
        try{
            String msgContainer;
            switch(currentState) {
                    case MANUAL:
                        if (ev instanceof MsgEventFromSerial) {
                            if (((MsgEventFromSerial) ev).getHeader(((MsgEventFromSerial) ev).getMsg()) != 'B') {
                                currentState = State.AUTOMATIC;
                            } else {
                                //Se ha l'header B prendi i restanti caratteri e invia il valore ad arduino.
                                String msgFromSerialWithoutHeader = ((MsgEventFromSerial) ev).getMsg().substring(1);
                                msgService.sendMsg(msgFromSerialWithoutHeader);
                            }
                        } else if (ev instanceof MsgEventFromWifi) {
                            //messaggio da wifi se è in manuale, cosa fare.
                            double msgFromWifi = Double.parseDouble((((MsgEventFromWifi) ev).getMsg()));
                            msgService.sendMsg(String.valueOf(msgFromWifi));
                        }
                        break;
                    case AUTOMATIC:
                        if (ev instanceof Tick) {
                            double value = 0.00;
                            String place = "Timer | Timer expired";
                            long time = System.currentTimeMillis();
                            JsonObject msg = new JsonObject()
                                    .put("value", value)
                                    .put("time", time)
                                    .put("place", place);
                            eventBus.publish("ErogationStop.new", msg);
                            System.out.println("[TICK] | Arrivato a: "+System.currentTimeMillis());
                            msgContainer = PCLOSE+String.valueOf(msgFromWifiEventBridge);
                            msgService.sendMsg(msgContainer);
                            pumpState= false;
                            timerIsAlive = false;
                            timer.stop();
                        }else if (ev instanceof MsgEventFromSerial) {
                            if (Objects.equals(((MsgEventFromSerial) ev).getMsg(), String.valueOf('B'))) {
                                System.out.println("[TIMER] | Partito a: "+ ((MsgEventFromSerial) ev).getMsg());
                                currentState = State.MANUAL;
                                msgService.sendMsg(BLUETOOTHCHANGESTATEMANUAL+ msgFromWifiEventBridge);
                            } else if (Objects.equals(((MsgEventFromSerial) ev).getMsg(), String.valueOf('A'))) {
                                currentState = State.AUTOMATIC;
                                msgService.sendMsg(BLUETOOTHCHANGESTATEAUTO+ msgFromWifiEventBridge);
                            }
                        }else if (ev instanceof MsgEventFromWifi) {
                            //messaggio da wifi se è in automatico, cosa fare.
                            String newMsg = ((MsgEventFromWifi) ev).getMsg();
                            double msgFromWifi = Double.parseDouble(newMsg);
                            msgFromWifiEventBridge = msgFromWifi;
                           // msgService.sendMsg(newMsg);
                            if (Objects.equals(oldMsgWitholdValue, newMsg)){
                                //lancia timer
                                if(!timerIsAlive && pumpState){
                                    timer.start(5000);
                                    System.out.println("[TIMER] | Partito a: "+ System.currentTimeMillis());
                                    timerIsAlive = true;
                                }
                                oldMsgWitholdValue = newMsg;
                            }  else {
                                //se i messaggi sono diversi e
                                //se c'è un timer lo chiudi
                                if(timerIsAlive){
                                    System.out.println("[TIMER] | Fermato a: "+System.currentTimeMillis());
                                    timer.stop();
                                    timerIsAlive = false;
                                    System.out.println("[TIMER] | Spengo Timer" );
                                }

                                if (msgFromWifi > (UMIN + DELTA)) {
                                    msgContainer = PCLOSE+String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                    pumpState = false;
                                } else if ((msgFromWifi <= (UMIN + DELTA)) && (msgFromWifi >= UMED)) {
                                    //Se è minore di 35 e maggiore di 20 mandi PMIN
                                    msgContainer = PMIN+String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                    pumpState = true;
                                } else if ((msgFromWifi < UMED) && (msgFromWifi >= UMAX)) {
                                    //Se è minore di 20 e maggiore di 10 mandi PMED
                                    msgContainer = PMED + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                    pumpState = true;
                                } else {
                                    //Se è minore di 10 e maggiore di 0 mandi PMAX
                                    msgContainer =  PMAX + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                    pumpState= true;
                                }
                                oldMsgWitholdValue = newMsg;
                            }
                        }
                        break;
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

