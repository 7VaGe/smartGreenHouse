package server;

import io.vertx.core.AbstractVerticle;
import java.net.InetAddress;
import java.net.UnknownHostException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Date;
import java.util.LinkedList;


import handleMsg.MyAgent;

/*
 * Data Service as a vertx event-loop 
 */
public class DataService extends AbstractVerticle {

	private int port;
	private static final int MAX_SIZE = 10;//Numero massimo di rilevamenti.
	private LinkedList<DataPoint> values;
	private static final String PORT = "COM4"; //porta arduino
	private static int BAUD = 9600;
	
	
	public DataService(int port) {
		values = new LinkedList<>();		
		this.port = port;
	}
	//MsgService ms= new MsgService(PORT, BAUD);
	MyAgent ma= new MyAgent(PORT, BAUD);
	
	public void start() {		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		
		router.post("/api/data").handler(this::handleAddNewData);
		router.get("/api/data").handler(this::handleGetData);
		router.route("/").handler(routingContext -> {
	        vertx.fileSystem().readFile("Resources/index.html", result -> {
	            if (result.succeeded()) {
	                routingContext.response()
	                    .putHeader("content-type", "text/html")
	                    .end(result.result());
	            } else {
	                routingContext.response()
	                    .setStatusCode(500)
	                    .end("Internal Server Error sdvdfsdfsdfd");
	            }
	        });
	    });
		
		
		vertx
			.createHttpServer()
			.requestHandler(router)
			.listen(port);
		router.route().failureHandler(this::handleFailure);
		/*
		 * Parte per l'aggiornamento automatico delle pagine web, non funzionante.
		 * */
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
		SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddress("dataUpdate"));
        sockJSHandler.bridge(options);
        
        router.route("/api/data").handler(sockJSHandler);
        // eventbus ancora non capito, ma sembra fondamentale.
		log("Service ready.");
	
		InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();         
            System.out.println("Your current IP address : " + ip); //l'indirizzo che mi serve da inseriere su arduino ide ESP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
	}
	/*
	 * */
	
	 private void updateData(long time, float value, String place) {
		 	JsonObject update = new JsonObject()
	               .put("time",time)
	               .put("value", value)
	               .put("place", place);               
	        vertx.eventBus().publish("dataUpdate", update);
	    }
	 
	private void handleFailure(RoutingContext routingContext) {
		   routingContext.response()
		     .putHeader("Content-type", "application/json; charset=utf-8")
		     .setStatusCode(500)
		     .end("internal server error");
		}
	
	private void handleAddNewData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		// log("new mess "+routingContext.getBodyAsString());
		JsonObject res =  routingContext.body().asJsonObject();
		if (res == null) {
			sendError(400, response);
		} else {
			float value = res.getFloat("value");
			String place = res.getString("place");
			long time = System.currentTimeMillis();
			
			values.addFirst(new DataPoint(value, time, place));
			if (values.size() > MAX_SIZE) {
				values.removeLast();
			}
			updateData(time,value,place);
			log("New value: " + value + " from " + place + " on " + new Date(time));        
			response.setStatusCode(200).end();
		}
	}
	
	private void handleGetData(RoutingContext routingContext) {
		JsonArray arr = new JsonArray();
		for (DataPoint p: values) {
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
	
	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void log(String string) {
		System.out.println("[DATA SERVICE] "+ string);
	}
}