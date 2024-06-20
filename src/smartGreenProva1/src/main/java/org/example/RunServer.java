package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class RunServer extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus();


        ServerService service = new ServerService(8080);
        vertx.deployVerticle(service);

        GreenHouseAgent greenHouseAgent = new GreenHouseAgent("COM4", 9600, eventBus);
        greenHouseAgent.start();
    }
}
