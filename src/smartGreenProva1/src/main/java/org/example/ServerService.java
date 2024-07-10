package org.example;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.http.HttpServerResponse;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;



public class ServerService extends AbstractVerticle {
    private final int port;
    private static final int MAX_SIZE = 10;// Max automation surveys number
    private static final int MAX_SIZE_BT = 5;// Max manual bluetooth surveys number
    private final LinkedList<DataPoint> values;
    private JsonObject lastSupplyData = new JsonObject();
    private final LinkedList<JsonObject> btValues;

    public ServerService(int port) {
        btValues = new LinkedList<>();
        values = new LinkedList<>();
        this.port = port;
    }
    /**
     * Start the main, create a server based on Vert.x
     *  configure the CORS setting to permit some request coming from unknown addresses, let those addresses reach this server,
     *  creates handlers for POST and GET request, if some request coming to us routers there are relative handlers based on the
     *  type of request and the server URI, the system anchor "index.html" as file route to be loaded first it's contains the web view.
     *  Create of HTTP server, and handle the errors with handler failure.
     * */
    public void start() {
    Router router = Router.router(vertx);

        // CORS Configuration
        router.route().handler(CorsHandler.create("http://192.168.1.12:8080")
                .allowCredentials(true)
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowedHeader("Access-Control-Allow-Method")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Content-Type"));

        router.route().handler(BodyHandler.create());

        router.post("/api/data").handler(this::handleAddNewData);
        router.get("/api/data").handler(this::handleGetData);
        router.post("/api/supplyState").handler(this::handleNewSupplyData);
        router.get("/api/supplyState").handler(this::handleSupplyGetDuration);
        router.post("/api/bluetooth").handler(this::handleNewBluetoothData);
        router.get("/api/bluetooth").handler(this::handleGetBluetoothData);

        router.route("/").handler(routingContext -> {
            vertx.fileSystem().readFile("index.html", result -> {
                if (result.succeeded()) {
                    routingContext.response()
                            .putHeader("content-type", "text/html")
                            .end(result.result());
                } else {
                    routingContext.response()
                            .setStatusCode(500)
                            .end("[SERVER] | Non trovo la risorsa");
                }
            });
        });

        router.route().failureHandler(this::handleFailure);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port);
        log("Ready.");
        /*
        * Function to know the right address of this machine, used to set the ESP module for establishing a Wi-Fi connection
        *
        * @return log of current ip address (localhost)
        *   */
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            log("Your current IP address : " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    /**
     * Extrapolate all the Json on payload at api/bluetooth server address, then use this values to create a new Json object
     *  used to maintain the current object in the global private array created with post request with all the data sent at /api/bluetooth,
     *  now implement the logic behind the supply view web server, this permit to understand the right supply time duration and when the pump is closed
     *  permit a more complete user experience.
     *
     * @param routingContext the context of this rote
     * */
    private void handleGetBluetoothData(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        for (int i = 0; i < btValues.size(); i++) {
            JsonObject currentData = btValues.get(i);
            JsonObject data = new JsonObject();

            long currentTime = currentData.getLong("time");
            data.put("time", currentTime);
            data.put("value", currentData.getValue("value"));
            data.put("place", currentData.getValue("place"));

            double value = currentData.getDouble("value");
            if(value == 0.0){
                data.put("supplyTime", "Pump is close!");
                arr.add(data);
            }else{
                if (i == 0) {
                    // Primo elemento: erogazione in corso o valore di default
                    data.put("supplyTime", "Supply in progress...");
                } else {
                    // Calcolo tempo di erogazione rispetto all'elemento precedente
                    JsonObject previousData = btValues.get(i - 1);
                    long previousTime = previousData.getLong("time");
                    double supplyDuration = (double) (previousTime- currentTime) / 1000;
                    data.put("supplyTime", supplyDuration);
                }
                arr.add(data);
            }
        }

        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(arr.encodePrettily());
    }
    /**
     * Extrapolate the body response as Json object at api/bluetooth server address, then insert this values on a LinkedList of Json objects,
     *  adding the value on top, simulate the queue mechanism, and will remove the last value on this LinkedList, this last value is the old value
     *  received from system.
     *  Set the status code 400 for failure and 200 to success.
     *
     * @param routingContext the context of this rote
     * */
    private void handleNewBluetoothData(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        JsonObject res = routingContext.body().asJsonObject();
        if (res == null) {
            sendError(400, response);
        }else{
            btValues.addFirst(res);
            if (btValues.size() > MAX_SIZE_BT) {
                btValues.removeLast();
            }
            response.setStatusCode(200).end();
        }
    }
    /**
     * Extrapolate the body response as Json object at api/supplyState server address, then save that value on lasSupplyData json object, to save the status and the
     *  closing time duration of water supply.
     *  Set the status code 400 for failure and 200 to success.
     *
     * @param routingContext the context of this rote
     * */
    private void handleNewSupplyData(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        JsonObject res = routingContext.body().asJsonObject();
        if (res == null) {
            sendError(400, response);
        }else{
            lastSupplyData = res;
            response.setStatusCode(200).end();
        }
    }
    /**
     * Once server receive a GET request use lasSupplyData json object,as return ended in json prettily mode.
     *
     * @param routingContext the context of this rote
     * */
    private void handleSupplyGetDuration(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(lastSupplyData.encodePrettily());
    }
    /**
     * Handle the failure received setting status 500 and printing the error log message.
     *
     * @param routingContext the context of this rote
     * */
    private void handleFailure(RoutingContext routingContext) {
        log("Handling failure: " + routingContext.failure());
        routingContext.response()
                .putHeader("Content-type", "application/json; charset=utf-8")
                .setStatusCode(500)
                .end("[SERVER] | Internal error: " + routingContext.failure().getMessage());
    }
    /**
     * Extrapolate the body response of a GET request as JSON object, then add this JSON object to
     *  JSON array and return it in json prettily mode.
     *
     * @param routingContext the context of this rote
     * */
    private void handleGetData(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        for (DataPoint p : values) {
            JsonObject data = new JsonObject();
            data.put("time", p.getTime());
            data.put("value", p.getValue());
            data.put("place", p.getPlace());
            arr.add(data);
        }
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(arr.encodePrettily());
    }
    /**
     * Extrapolate the body response of a POST request as JSON object, then add all those values to an JSON object
     *  that will be sent to a specific route, if the JSON object response's place value is "Bluetooth" will print
     *  at console the information about the value received, otherwise value is "home" send this value to eventbus
     *  for being processed, the last if value is "Server" the system will print the supply interruption,
     *  is provided from tick event.
     *  Then this handler, add data on a LinkedList to store at maximum of 10 surveys, and set status 200 on success.
     *
     * @param routingContext the context of this rote
     * */
    private void handleAddNewData(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        JsonObject res = routingContext.body().asJsonObject();
        if (res == null) {
            sendError(400, response);
        } else {
            double value = res.getDouble("value");
            String place = res.getString("place");
            long time = System.currentTimeMillis();
            JsonObject event = new JsonObject()
                    .put("value", value)
                    .put("time", time)
                    .put("place", place);
            switch(place){
                case "Bluetooth": System.out.println("[BLUETOOTH] | Received value: " + value);
                        log("New value: " + value + " from " + place + " on " + new Date(time));
                    break;
                case "ESP": vertx.eventBus().publish("data.new", event);
                        log("New value: " + value + " from " + place + " on " + new Date(time));
                    break;
                case "Server": System.out.println("[SERVER] | Supply interrupted, time expired");
                    break;
            }
            values.addFirst(new DataPoint(value, time, place));
            if (values.size() > MAX_SIZE) {
                values.removeLast();
            }
            response.setStatusCode(200).end();
        }
    }
    /**
     * Sending function for errors received
     *
     * @param statusCode the status code of the relative response
     * @param response the http server response
     * */
    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
    /***
     * Log function o print
     *
     * @param string message to print
     */
    private void log(String string) {
        System.out.println("[SERVER] | " + string);
    }
}
