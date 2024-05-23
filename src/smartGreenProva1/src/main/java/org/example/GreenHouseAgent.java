package org.example;


import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Timer;

public class GreenHouseAgent extends BasicEventLoopController {
    private final MsgService msgService;
    private EventBus eventBus;


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
    private static final int PMIN= 3;
    private static final int PMED= 2;
    private static final int PMAX= 1;
    private static final int PCLOSE= 4;
    private String oldMsgWitholdValue = "";
    private ObservableTimer timer;
    private boolean timerIsAlive = false;

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
                        if (ev instanceof Tick) { //controllare bene questa parte sembra che ci siano delle cose che non vanno.
                            double value = 0.00;
                            String place = "Timer | Timer expired";
                            long time = System.currentTimeMillis();
                            JsonObject msg = new JsonObject()
                                    .put("value", value)
                                    .put("time", time)
                                    .put("place", place);
                            eventBus.publish("ErogationStop.new", msg);
                            System.out.println("[TICK] | Arrivato a: "+System.currentTimeMillis());
                            System.out.println("[EVENTO TICK] | Chiudo pompa e spengo il Timer: "+ timer.toString());
                            msgService.sendMsg(String.valueOf(PCLOSE));
                            timerIsAlive = false;
                            timer.stop();
                        }else if (ev instanceof MsgEventFromSerial) {
                            if (((MsgEventFromSerial) ev).getHeader(((MsgEventFromSerial) ev).getMsg()) == ('B')) {
                                currentState = State.MANUAL;
                            }
                        }else if (ev instanceof MsgEventFromWifi) {
                            //messaggio da wifi se è in automatico, cosa fare.
                            String newMsg = ((MsgEventFromWifi) ev).getMsg();
                            if (Objects.equals(oldMsgWitholdValue, newMsg)){
                                //lancia timer
                                if(!timerIsAlive){
                                    System.out.println("[TIMER] | Avvio Timer: " + timer.toString() );
                                    timer.scheduleTick(5000);
                                    System.out.println("[TIMER] | Partito a: "+ System.currentTimeMillis());
                                    timerIsAlive = true;
                                }
                                oldMsgWitholdValue = ((MsgEventFromWifi) ev).getMsg();
                            } else {
                                //se c'è un timer lo chiudi
                                if(timerIsAlive){
                                    System.out.println("[TIMER] | Fermato a: "+System.currentTimeMillis());
                                    timer.stop();
                                    timerIsAlive = false;
                                    System.out.println("[TIMER] | Spengo Timer: " + timer.toString() );
                                }
                                oldMsgWitholdValue = newMsg;
                            }
                            double msgFromWifi = Double.parseDouble(newMsg);
                            if (msgFromWifi > (UMIN + DELTA)) {
                                msgService.sendMsg(String.valueOf(PCLOSE));
                            } else if ((msgFromWifi <= (UMIN + DELTA)) && (msgFromWifi >= UMED)) {
                                //Se è minore di 35 e maggiore di 20 mandi PMIN
                                msgService.sendMsg(String.valueOf(PMIN));
                            } else if ((msgFromWifi < UMED) && (msgFromWifi >= UMAX)) {
                                //Se è minore di 20 e maggiore di 10 mandi PMED
                                msgService.sendMsg(String.valueOf(PMED));
                            } else {
                                //Se è minore di 10 e maggiore di 0 mandi PMAX
                                msgService.sendMsg(String.valueOf(PMAX));
                            }
                        }
                        break;
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

