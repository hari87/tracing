package nl.hari;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
//@CamelOpenTracing
public class TraceExampleApplication {
//	@EndpointInject(uri = "direct:testMsg")
	private FluentProducerTemplate producer;

	public static void main(String[] args) {
		SpringApplication.run(TraceExampleApplication.class, args);
	}

	/*@RequestMapping("/greeting")
	public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
		String resp = producer.withHeader("request_name", name).request(String.class);
		return resp;
	}*/

}
