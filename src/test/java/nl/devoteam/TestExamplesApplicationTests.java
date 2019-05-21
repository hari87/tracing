package nl.devoteam;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestExamplesApplication.class},
		properties = { "camel.springboot.java-routes-include-pattern=**/BookstoreImpl*"})
@UseAdviceWith
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
public class TestExamplesApplicationTests {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private ProducerTemplate template;

	@Test
	public void testDirectFindBooks() throws Exception{
		camelContext.getRouteDefinition("UpdatecallCount")
				.adviceWith(camelContext, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {

						weaveAddLast().to("mock:result");


					}
				});
		camelContext.start();
		//check if the endpoint is loaded.
		assertThat(camelContext.getEndpoint("direct:UpdatecallCount")).isNotNull();
		MockEndpoint mockOut = camelContext.getEndpoint("mock:result", MockEndpoint.class);
		mockOut.expectedMessageCount(1);
		mockOut.expectedBodiesReceived("hello");
		template.sendBody("direct:UpdatecallCount","");
		mockOut.assertIsSatisfied();

	}
//check if the body contains certain text.

	@Test
	public void testDirectFetchSampleBooks() throws Exception{

		camelContext.getRouteDefinition("FetchSampleBooks")
				.adviceWith(camelContext, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						weaveAddLast().to("mock:result");

					}
				});
		camelContext.start();

		assertThat(camelContext.getEndpoint("direct:FetchSampleBooks")).isNotNull();
		MockEndpoint mockOut = camelContext.getEndpoint("mock:result", MockEndpoint.class);
		mockOut.expectedMessageCount(1);
		mockOut.message(0).body().toString().contains("of");
		template.sendBody("direct:FetchSampleBooks","");
		mockOut.assertIsSatisfied();

	}
// stub a service - mere information purposes only.
	@Test
	public void testStubExample() throws Exception{

		camelContext.getRouteDefinition("queue_test")
				.adviceWith(camelContext, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						weaveAddFirst().to("mock:captureFirst");
						weaveAddLast().to("mock:result");

					}
				});
		camelContext.start();
		MockEndpoint mockIn = camelContext.getEndpoint("mock:captureFirst", MockEndpoint.class);
		MockEndpoint mockOut = camelContext.getEndpoint("mock:result", MockEndpoint.class);
		mockOut.expectedMessageCount(1);
		mockIn.expectedMessageCount(1);
		mockIn.message(0).body().toString().equals("unit-test");
		mockOut.message(0).body().toString().contains("message");
		template.sendBody("stub:activemq:queue:test_queue", "unit-test");
		mockIn.assertIsSatisfied();
		mockOut.assertIsSatisfied();

	}

	@Test
	public void replaceFrom() throws Exception{

		camelContext.getRouteDefinition("intercept_ep_test")
				.adviceWith(camelContext, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {

						weaveById("git_ep_id").replace().setBody(new ConstantExpression("response from github"));
						weaveAddLast().to("mock:result");

					}
				});
		camelContext.start();
		MockEndpoint mockOut = camelContext.getEndpoint("mock:result", MockEndpoint.class);
		mockOut.expectedMessageCount(1);
		mockOut.message(0).body().toString().contains("response");
		template.sendBody("direct:test_replace", "unit-test");
		mockOut.assertIsSatisfied();

	}

	@Test
	public void replaceFrom2() throws Exception{

		camelContext.getRouteDefinition("intercept_ep_test")
				.adviceWith(camelContext, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						interceptSendToEndpoint("https://api.github.com/users/octocat/orgs")
								.skipSendToOriginalEndpoint()
								.to("mock:catchGitMsg");
						weaveById("git_ep_id").replace().setBody(new ConstantExpression("response from github"));
						weaveAddLast().to("mock:result");

					}
				});
		camelContext.start();
		//check if the endpoint is loaded.
		//assertThat(camelContext.getEndpoint("direct:FetchSampleBooks")).isNotNull();
		MockEndpoint mockHttp = camelContext.getEndpoint("mock:catchGitMsg", MockEndpoint.class);
		MockEndpoint mockOut = camelContext.getEndpoint("mock:result", MockEndpoint.class);
		mockOut.expectedMessageCount(1);
		mockHttp.whenAnyExchangeReceived(e->e.getIn().setBody("replace unit message"));
		mockOut.message(0).body().toString().contains("unit");
		template.sendBody("direct:test_replace", "unit-test");
		mockOut.assertIsSatisfied();
	}

}

