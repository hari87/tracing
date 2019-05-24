package nl.hari;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InvokeTraceServerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:routeMessageFromController")
                .log("inside route ${headers}")
                .toD("http://localhost:8080/trace-server")
                .log("${body}");
    }
}
