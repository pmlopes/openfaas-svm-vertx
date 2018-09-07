import io.vertx.ext.web.RoutingContext;

import xyz.jetdrone.openfaas.vertx.OpenFaaS;

public class MyFunction implements OpenFaaS {

  public MyFunction() {
    System.out.println("Loaded MyFunction!");
  }

  @Override
  public void handle(RoutingContext req) {
		req.response().end("Hi!");
	}

}
