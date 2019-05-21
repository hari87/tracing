package nl.devoteam.swagger.api;

import nl.devoteam.swagger.model.Book;
import org.apache.camel.Body;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BookstoreImpl  extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:findBooks")
                .to("direct:FetchSampleBooks")
                .to("direct:UpdatecallCount");

        from("direct:FetchSampleBooks")
                .routeId("FetchSampleBooks")
                .setBody()
                .method(BookstoreImpl.class, "mockBookData")
                .to("log:nl.devoteam.swagger.model.Book?showAll=true");

        from("direct:UpdatecallCount")
                .routeId("UpdatecallCount")
                .setBody()
                .constant("hello")
                .log("a book has been searched");

        from("stub:activemq:queue:test_queue")
                .routeId("queue_test")
                .setBody()
                .constant("message in queue")
                .log("${body}");

        from("activemq:queue:test_queue")
                .routeId("replace_from_test")
                .setBody()
                .constant("message in queue")
                .log("${body}");

        from("direct:test_replace")
                .routeId("intercept_ep_test")
                .setBody()
                .constant("message in queue")
                .to("https://api.github.com/users/octocat/orgs").id("git_ep_id")
                .log("${body}");
    }





    public Book mockBookData(){
        Book book = new Book();
        book.setBookDescription("Magic is important");
        book.setBookInventory(2);
        book.setBookIsbn("ffu-341-tyhka");
        book.setBookTitle("Prisoner of Azkaban");
        return book;
    }


}
