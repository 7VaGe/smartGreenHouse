package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class  RunServer extends AbstractVerticle {
   private final static String PORT = "COM4"; //Change this port to right one

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus();

        ServerService service = new ServerService(8080);
        vertx.deployVerticle(service);

        GreenHouseAgent greenHouseAgent = new GreenHouseAgent(PORT, 9600, eventBus, vertx);
        greenHouseAgent.start();
    }
}
