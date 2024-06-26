package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/*
 * Data Service as a vertx event-loop 
 */
public class RunService extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		DataService service = new DataService(8080);
		vertx.deployVerticle(service);
	}
}