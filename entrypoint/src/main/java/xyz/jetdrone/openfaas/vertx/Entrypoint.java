/*
 * Copyright 2018 Red Hat, Inc.
 *
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
 */
package xyz.jetdrone.openfaas.vertx;

import java.util.ServiceLoader;
import java.util.function.Consumer;

import io.vertx.core.Vertx;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Entrypoint {

  private static int getListenPort() {
    int port = 8000;

    String env = System.getenv("http_port");

    if (env != null) {
      port = Integer.parseInt(env);
    }

    return port;
  }

  public static void main(String[] args) {
    final int port = getListenPort();

    final Vertx vertx = Vertx.vertx();
    final Router app = Router.router(vertx);

    app.route().handler(BodyHandler.create());

    ServiceLoader<Consumer> serviceLoader = ServiceLoader.load(Consumer.class);
    for (Consumer fn : serviceLoader) {
      app.route().handler(fn::accept);
    }

    vertx.createHttpServer()
      .requestHandler(app)
      .listen(port, listen -> {
        if (listen.succeeded()) {
          System.out.println("OpenFaaS Vert.x listening on port: " + port);
        } else {
          listen.cause().printStackTrace();
          System.exit(1);
        }
      });
  }
}
