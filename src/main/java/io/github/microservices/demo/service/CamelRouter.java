package io.github.microservices.demo.service;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A simple Camel REST DSL route that implements the arrivals service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        String socksCatalogueHost = "localhost:8080"; //System.getenv("SOCKS_CATALOGUE_HOST");
        String shoesCatalogueHost = "localhost:8082"; //System.getenv("SHOES_CATALOGUE_HOST");
        
        if (socksCatalogueHost == null || socksCatalogueHost.isEmpty()) {
            throw new Exception("SOCKS_CATALOGUE_HOST env var not set");
        } else if(shoesCatalogueHost == null || shoesCatalogueHost.isEmpty()) {
            throw new Exception("SHOES_CATALOGUE_HOST env var not set");
        }

        // @formatter:off
        restConfiguration()
            .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Shop Catalogue Aggregator REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiProperty("schemes", "https")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/catalogue")
            .description("List all products (socks & shoes) from the shop catalogue database")
            .get()
            .param().name("user_key")
                .type(RestParamType.query)
                .required(false)
                .description("User Key, if calling the API in front of 3Scale.")
                .endParam()
            .outType(ProductsList.class)
            .route().routeId("catalogue-api")
            .multicast(new ProductAggregationStrategy())
            .parallelProcessing()

            // remote services
            .to("direct:socksImplRemote", "direct:shoesImplRemote");
    
        from("direct:socksImplRemote").description("Socks catalogue REST service implementation route")
            .log("Calling socks endpoint...")
            .streamCaching()
            .to(String.format("http://%s/catalogue?bridgeEndpoint=true", socksCatalogueHost))
            .convertBodyTo(String.class)
            // .log("Sock raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, SocksList.class);
    
        from("direct:shoesImplRemote").description("Shoes catalogue REST service implementation route")
            .log("Calling shoes endpoint...")
            .streamCaching()
            .to(String.format("http://%s/shoes?bridgeEndpoint=true", shoesCatalogueHost))
            .convertBodyTo(String.class)
            // .log("Shoe raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, ShoesList.class);
    
        // @formatter:on
    }

}