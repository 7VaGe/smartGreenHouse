package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;


public class ServerService extends AbstractVerticle {

    private final int port;
    private static final int MAX_SIZE = 10; // Numero massimo di rilevamenti
    private final LinkedList<DataPoint> values;

    public ServerService(int port) {
        values = new LinkedList<>();
        this.port = port;
    }

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

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port);

        router.route().failureHandler(this::handleFailure);

        PermittedOptions permittedOptions = new PermittedOptions()
                .setAddress("dataUpdate")
                .setAddress("ErogationStop.new")
                .setAddress("manualValue.new");

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addOutboundPermitted(permittedOptions)
                .addInboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
        router.route("/eventbus/*").handler(sockJSHandler);

        router.route().handler(BodyHandler.create());
        router.post("/api/data").handler(this::handleAddNewData);
        router.get("/api/data").handler(this::handleGetData);

        router.route().handler(routingContext -> {
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

        log("Ready.");

        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            log("Your current IP address : " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        vertx.eventBus().consumer("ErogationStop.new", msg -> {
            JsonObject jsonObject = (JsonObject) msg.body();
            vertx.eventBus().publish("dataUpdate", jsonObject);
        });

        vertx.eventBus().consumer("manualValue.new", msg -> {
            JsonObject jsonObject = (JsonObject) msg.body();
            vertx.eventBus().publish("dataUpdate", jsonObject);
        });
    }

    private void handleFailure(RoutingContext routingContext) {
        log("Handling failure: " + routingContext.failure());
        routingContext.response()
                .putHeader("Content-type", "application/json; charset=utf-8")
                .setStatusCode(500)
                .end("[SERVER] | Internal error: " + routingContext.failure().getMessage());
    }

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
            vertx.eventBus().publish("data.new", event);

            values.addFirst(new DataPoint(value, time, place));
            if (values.size() > MAX_SIZE) {
                values.removeLast();
            }
            updateData(time, value, place);
            log("New value: " + value + " from " + place + " on " + new Date(time));
            response.setStatusCode(200).end();
        }
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void updateData(long time, double value, String place) {
        JsonObject update = new JsonObject()
                .put("time", time)
                .put("value", value)
                .put("place", place);
        vertx.eventBus().publish("/dataUpdate", update);
    }

    private void log(String string) {
        System.out.println("[SERVER] | " + string);
    }
}
