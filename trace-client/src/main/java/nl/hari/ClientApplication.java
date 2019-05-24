/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.hari;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.ClientEndpoint;
import java.util.Map;

@CamelOpenTracing
@Configuration
@EnableAutoConfiguration
@ComponentScan
@RestController
public class ClientApplication {

	private static Log logger = LogFactory.getLog(ClientApplication.class);
	@EndpointInject(uri = "direct:routeMessageFromController")
	private FluentProducerTemplate producer;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClientApplication.class, args);
	}

	@RequestMapping("/trace-client")
	public String getResponse(@RequestHeader Map<String, String> headers){

		headers.forEach((key, value) -> {
			logger.info(String.format("Header '%s' = %s", key, value));
		});
		producer.withHeader("call_app", "myApp").request();

		return "check for tracing";
	}



}
