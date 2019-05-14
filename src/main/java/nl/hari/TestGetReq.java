package nl.hari;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class TestGetReq extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getContext().addComponent("insecurehttps4", new InsecureHttps4Component());

        from("timer:myTimer?repeatCount=1")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("insecurehttps4://appointment-system-api-nl-appointment-dev.apps.npd.msa.libgbl.biz/appointment/ADT_NL_1078625")
                .log("${body}");
    }
}
