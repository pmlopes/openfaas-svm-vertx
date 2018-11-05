## Template: openfaas-svm-template

The GraalVM SVM (Eclipse Vert.x native image) template uses maven as a build system.

The template makes use of the OpenFaaS incubator project [of-watchdog](https://github.com/openfaas-incubator/of-watchdog).

### Structure

This a template showcasing the use of GraalVM/SubstrateVM and Eclipse Vert.x as a viable runtime for Serverless functions.

Functions are pure java `xyz.jetdrone.openfaas.vertx.OpenFaaS` implementations. The minimal function can be:

```java
import io.vertx.ext.web.RoutingContext;
import xyz.jetdrone.openfaas.vertx.OpenFaaS;

public class MyFunction implements OpenFaaS {
  @Override
  public void handle(RoutingContext ctx) {
		ctx.response().end("Hi!");
	}
}
```

The provided entrypoint will setup the required HTTP server and will handle all the upload parsing making it available on the
`RoutingContext` of the function.

### Trying the template

```
$ faas template pull https://github.com/pmlopes/openfaas-svm-vertx
$ faas new --lang vertx-svm <fn-name>
```

### Building

When working in development mode, the java application is build as usual:

```
mvn clean package
```

When going to OpenFAAS, the build is run inside a Docker container using the provided `Dockerfile`.

The Dockerfile performs the following actions:

1. Builds the Java code
2. Generates a native image from the fat jar
3. Creates a new image and install OpenFAAS of-watchdog
4. Copies the native image to the new container
5. Configures the watchdog

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
