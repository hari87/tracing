package nl.hari;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InvokeRandomService extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("direct:routeMessageFromController")
                .log("inside route ${headers}")
                .toD("http://dummy.restapiexample.com/api/v1/employees")
                .log("${body}");
    }
}
