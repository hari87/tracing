package nl.devoteam.swagger.api;

import javax.annotation.Generated;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

/**
 * Generated from Swagger specification by Camel REST DSL generator.
 */
@Generated("org.apache.camel.generator.swagger.PathGenerator")
@Component
public final class Bookstore extends RouteBuilder {
    /**
     * Defines Apache Camel routes using REST DSL fluent API.
     */
    public void configure() {

        restConfiguration().component("restlet")
                .host("localhost")
                .port("8085")
                .bindingMode(RestBindingMode.auto)
                .contextPath("/").apiContextPath("/api-doc");

        rest("/bookstore")
            .get("/books")
                .id("findBooks")
                .description("Returns all the books from the system")
                .param()
                    .name("book_isbn")
                    .type(RestParamType.query)
                    .dataType("string")
                    .required(false)
                    .description("isbn used to filter the result")
                .endParam()
                .to("direct:findBooks")
            .post("/books")
                .id("addBook")
                .description("Add a new book to the collection")
                .param()
                    .name("book")
                    .type(RestParamType.body)
                    .required(false)
                    .description("The Book JSON you want to post")
                .endParam()
                .to("direct:addBook")
            .get("/books/{book_isbn}")
                .description("Returns all the books from the system")
                .param()
                    .name("book_isbn")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                    .description("isbn of the book")
                .endParam()
                .to("direct:rest1");
    }
}
