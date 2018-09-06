## Template: openfaas-svm-template

The GraalVM SVM (Eclipse Vert.x native image) template uses maven as a build system.

### Structure

This a template showcasing the use of GraalVM/SubstrateVM and Eclipse Vert.x as a viable runtime for Serverless functions.

Functions are pure java `io.vertx.core.Handler<io.vertx.ext.web.RoutingContext>` implementations. The minimal function can be:

```java
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class Function implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext ctx) {
    ctx.response().end("OK");
  }
}
```

The provided entrypoint will setup the required HTTP server and will handle all the upload parsing making it available on the
`RoutingContext` of the function.

#### Development

Developing functions will follow the traditional java development model and the maven POM model. Dependencies and Tests can be
added to the `pom.xml` file as usual plus tests can be added to `src/test/java`.

### Building

When working in development mode, the java application is build as usual:

```
./mvnw clean package
```

When going to OpenFAAS, the build is run inside a Docker container using the provided `Dockerfile`.

The Dockerfile performs the following actions:

1. Downloads GraalVM CE
2. Sets the GraalVM as the default Java SDK
3. Builds the Java code
4. Generates a native image from the fat jar
5. Creates a new image and install OpenFAAS of-watchdog
6. Copies the native image to the new container
7. Configures the watchdog

You can also run the Dockerfile locally:

```
docker build -t my-vertx-fn .
docker run --rm -p 8080:8080 my-vertx-fn
```

You can observe instant startup times and low memory usage:

```
Forking - vertx-fn []
2018/09/06 17:54:31 Started logging stderr from function.
2018/09/06 17:54:31 OperationalMode: http
2018/09/06 17:54:31 Started logging stdout from function.
2018/09/06 17:54:31 Writing lock file at: /tmp/.lock
2018/09/06 17:54:31 stdout: OpenFaaS Vert.x listening on port: 8000
```

```
docker stats
CONTAINER ID        NAME                CPU %               MEM USAGE / LIMIT     MEM %               NET I/O             BLOCK I/O           PIDS
cbcb653158fe        priceless_volhard   0.70%               5.812MiB / 15.55GiB   0.05%               3.09kB / 0B         0B / 0B             10
```

Remember that the memory usage in this case accounts for both the vert.x application and the watchdog!
