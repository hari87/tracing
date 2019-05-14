package nl.hari;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleRoute extends RouteBuilder {

    @Value("${datasource.name}")
    private String valFromYaml;

    @Value("${datasource.sample}")
    private String valFromSample;

    @Override
    public void configure() throws Exception {

        //from("direct:testMsg")
        from("timer:myTimer?repeatCount=1")
                .log("inside camel route")
                .setBody(simple("${header.request_name} world :"+valFromYaml+"\n loading val from sample :"+valFromSample));
    }
}
