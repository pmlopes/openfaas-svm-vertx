/*
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */import io.vertx.ext.web.RoutingContext;

import xyz.jetdrone.openfaas.vertx.OpenFaaS;

public class MyFunction implements OpenFaaS {

  public MyFunction() {
    System.out.println("Loaded MyFunction!");
  }

  @Override
  public void handle(RoutingContext ctx) {
		ctx.response().end("Hi!");
	}

}
