package org.example;


import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.LinkedList;
import java.util.Objects;

public class GreenHouseAgent extends BasicEventLoopController {
    private final MsgService msgService;
    private final EventBus eventBus;
    private final HttpClient httpClient;

    @Override
    public boolean notifyEvent(Event ev) {
        processEvent(ev);
        return true;
    }
    private static final String PUMPCLOSE= "Pump is Closed";
    private static final String PUMPOPEN= "Pump is Open";
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
    private static final String BYPASSARDUINO = "r";
    private static final String PUMPHANDLER = "p";
    private static final String ZERO = "0";
    private String oldMsgWithOldValue = "";
    private final ObservableTimer timer;
    private double msgFromWifiEventBridge;
    private static double supplyTimeStart;
    private static boolean cmdReceived = false;
    private static int lastManualBtValue = 0;


    /**
     * Agent element that perform the message server logics, this Agent communicates with server using EventBus and Http request.
     *
     * @param port Serial Channel port number
     * @param vertx Server router
     * @param rate Serial Channel baud rate
     * @param eventBus vert.x eventbus used to share resources with others components
     * */
    public GreenHouseAgent(String port, int rate, EventBus eventBus, Vertx vertx){
        super();
        this.msgService = new MsgService(port, rate);
        msgService.init();
        msgService.addObserver(this);
        this.timer = new ObservableTimer();
        timer.addObserver(this);
        currentState = State.AUTOMATIC;
        this.eventBus = eventBus;
        this.httpClient = vertx.createHttpClient();


        eventBus.consumer("data.new", msg -> {
            JsonObject event = (JsonObject) msg.body();
            double value = event.getDouble("value");
            processEvent(new MsgEventFromWifi(Double.toString(value)));
        });
    }
    /**
     * Function to create an httpPost Request
     *
     * @param port Localhost port number, set to 8080
     * @param client Vert.x http client
     * @param host Localhost address
     * @param httpMethod Request method {POST, GET, ..}
     * @param msg The message to send through Http Post
     * @param uri The server URI, where the msg is delivered to
     * */
    public void httpPostRequest (HttpMethod httpMethod, HttpClient client,int port, String host, String uri, JsonObject msg){
        client.request(httpMethod, port, host, uri)
                .compose(request -> {
                    request.putHeader("content-type", "application/json");
                    return request.send(msg.toBuffer());
                })
                .onSuccess(response -> {
                    response.body()
                            .onFailure(Throwable::printStackTrace);
                })
                .onFailure(Throwable::printStackTrace);
    }
    /**
     * Processes the event, checking the currentState.
     * If the Application is in a Manual State:
     *      check the origin of event, if it comes from a Wi-Fi o Serial there are two different logic.
     *      On Wi-Fi the function send through Serial Channel the message received putting a header before for bypassing arduino and
     *      go directly to the view, by bluetooth.
     *      On Serial the function checks the ev headers and the ev payload, if the ev headers starts with 'A' the application is set to Automatic Mode,
     *      otherwise, it's check the payload {"op" = open pump, "cp"= close pump} used for monitoring the pump state.
     *
     * If the Application is in an Automatic State:
     *      check the event if is an instance of Tick, then creates a new JSON object with the event keys-values, this Json object will be post on
     *      a specific route URI on the server, and send another JSON object with supplyState to Serial Channel the close command to interrupt the water supply and notify to another URI (/api/supplyState)
     *      the interrupt event.
     *      As the Manual state logic the application checking the origin of the event, if this event ev is an instance of serial event, checking the headers and payload, to set a cmd received flag, that indicates
     *      if the command to open servo/pump is received or not, this flag used in combo with OP and CP event message, it allows the system to know if it has already received an open pump or closed pump command previously asd then set the current
     *      status to the web view.
     *      Otherwise, once the system receive a Wi-Fi event message, extrapolate it, and then it use that humidity value to calculate the right water supply value to send to arduino.
     *
     * @param ev The event to process
     * */
    @Override
    protected void processEvent(Event ev) {
        try{
            String msgContainer;
            switch(currentState) {
                    case MANUAL:
                        if (ev instanceof MsgEventFromSerial) {
                            cmdReceived = false;
                            if (((MsgEventFromSerial) ev).getHeader(((MsgEventFromSerial) ev).getMsg()) == 'A') {
                                currentState = State.AUTOMATIC;
                                msgService.sendMsg(BLUETOOTHCHANGESTATEAUTO);
                            } else if (((MsgEventFromSerial) ev).getMsg().startsWith("cp")) {
                                cmdReceived = false;
                                JsonObject msgClosePump = new JsonObject().put("state", PUMPCLOSE).put("supplyTime", (System.currentTimeMillis() - supplyTimeStart) / 1000);
                                httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/supplyState", msgClosePump);
                            } else if (((MsgEventFromSerial) ev).getMsg().startsWith("op") && !cmdReceived) {
                                cmdReceived = true;
                                supplyTimeStart = System.currentTimeMillis();
                                JsonObject msgOpenPump = new JsonObject().put("state", PUMPOPEN);
                                httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/supplyState", msgOpenPump);
                            } else {
                                //Se ha l'header B prendi i restanti caratteri e invia il valore ad arduino.
                                System.out.println("[SERIAL MANUAL] | RICEIVED FROM ARDUINO: " + ((MsgEventFromSerial) ev).getMsg());
                                //se è una notifica di errore, stampala a video e chiudi la pompa.
                                if (((MsgEventFromSerial) ev).getMsg().startsWith("[ERROR]")) {
                                    //notifico il mio errore a video, e chiudo la pompa
                                    System.out.println("[ARDUINO] | " + ((MsgEventFromSerial) ev).getMsg());
                                    msgService.sendMsg(ZERO);
                                } else if(lastManualBtValue != Integer.parseInt(((MsgEventFromSerial) ev).getMsg()) && !cmdReceived){
                                    lastManualBtValue = Integer.parseInt(((MsgEventFromSerial) ev).getMsg());
                                    msgService.sendMsg(PUMPHANDLER + ((MsgEventFromSerial) ev).getMsg());
                                    double value = Double.parseDouble(((MsgEventFromSerial) ev).getMsg());
                                    JsonObject newDataFromSerial = new JsonObject();
                                    String place = "Bluetooth";
                                    long time = System.currentTimeMillis();
                                    newDataFromSerial.put("value", value)
                                            .put("time", time)
                                            .put("place", place)
                                            .put("erogationTime", "Dispensing in progress...");
                                    httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/bluetooth", newDataFromSerial);
                                }
                            }
                        } else if (ev instanceof MsgEventFromWifi) {
                            msgService.sendMsg(BYPASSARDUINO + (((MsgEventFromWifi) ev).getMsg()));
                        }
                        break;
                    case AUTOMATIC:
                        if (ev instanceof Tick) {
                            double value = 0.00;
                            String place = "Server";
                            long time = System.currentTimeMillis();
                            JsonObject msg = new JsonObject()
                                    .put("value", value)
                                    .put("time", time)
                                    .put("place", place);
                            httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/data", msg );
                            System.out.println("[TICK] | Arrivato a: "+System.currentTimeMillis());
                            msgContainer = PCLOSE+String.valueOf(msgFromWifiEventBridge);
                            JsonObject msgClosePump =  new JsonObject().put("state", PUMPCLOSE).put("supplyTime", (System.currentTimeMillis() - supplyTimeStart)/1000);
                            httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/supplyState", msgClosePump );
                            msgService.sendMsg(msgContainer);
                            timer.stop();
                        }else if (ev instanceof MsgEventFromSerial) {
                            if (Objects.equals(((MsgEventFromSerial) ev).getMsg(), String.valueOf('B'))) {
                                currentState = State.MANUAL;
                                msgService.sendMsg(BLUETOOTHCHANGESTATEMANUAL+ msgFromWifiEventBridge);
                            } else if (((MsgEventFromSerial) ev).getMsg().startsWith("cp")) {
                                cmdReceived =  false;
                                JsonObject msgClosePump =  new JsonObject().put("state", PUMPCLOSE).put("supplyTime", (System.currentTimeMillis() - supplyTimeStart)/1000);
                                httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/supplyState", msgClosePump );
                                //System.out.println("[TIMER] | Timer stop: " + System.currentTimeMillis());debug timer print
                                timer.stop();
                            }else if(((MsgEventFromSerial) ev).getMsg().startsWith("op") && !cmdReceived){
                                cmdReceived =  true;
                                supplyTimeStart = System.currentTimeMillis();
                                JsonObject msgOpenPump =  new JsonObject().put("state", PUMPOPEN);
                                httpPostRequest(HttpMethod.POST, httpClient, 8080, "localhost", "/api/supplyState", msgOpenPump );
                                timer.start((5000));
                                //System.out.println("[TIMER] | Started at: " + System.currentTimeMillis()); debug timer print
                            }
                        }else if (ev instanceof MsgEventFromWifi) {
                            String newMsg = ((MsgEventFromWifi) ev).getMsg();
                            double msgFromWifi = Double.parseDouble(newMsg);
                            msgFromWifiEventBridge = msgFromWifi;
                            if (Objects.equals(oldMsgWithOldValue, newMsg)) {
                                oldMsgWithOldValue = newMsg;
                            } else {
                                if (msgFromWifi > (UMIN + DELTA)) {
                                    msgContainer = PCLOSE + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                } else if ((msgFromWifi <= (UMIN + DELTA)) && (msgFromWifi >= UMED)) {
                                    msgContainer = PMIN + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                } else if ((msgFromWifi < UMED) && (msgFromWifi >= UMAX)) {
                                    msgContainer = PMED + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                } else {
                                    msgContainer = PMAX + String.valueOf(msgFromWifi);
                                    msgService.sendMsg(msgContainer);
                                }
                                oldMsgWithOldValue = newMsg;
                            }
                        }
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

