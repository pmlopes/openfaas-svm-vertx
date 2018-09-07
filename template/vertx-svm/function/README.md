## Vert.x SVM Function

Implement your functions [MyFunction](src/main/java/MyFunction.java):

```java
public class MyFunction implements xyz.jetdrone.openfaas.vertx.OpenFaaS {
  @Override
  public void handle(io.vertx.ext.web.RoutingContext ctx) {
		ctx.response().end("Hi!");
	}
}
```

If you rename the function file you will need to update the `META-INF/services` file with the new name **as well as** the `src/main/svm/reflection.json`.
