package server;

import io.vertx.core.*;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {
	  @Override
	  public void start() throws Exception {
	    // Create a Router
	    Router router = Router.router(vertx);

	   
	    // Create the HTTP server
	    vertx.createHttpServer()
	      // Handle every request using the router
	      .requestHandler(router)
	      // Start listening
	      .listen(8080)
	      // Print the port
	      .onSuccess(server ->
	        System.out.println(
	          "HTTP server started on port " + server.actualPort()
	        )
	      );
	  }
	}