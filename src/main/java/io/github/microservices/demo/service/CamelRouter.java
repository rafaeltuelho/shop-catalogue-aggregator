package io.github.microservices.demo.service;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A simple Camel REST DSL route that implements the arrivals service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {

    @Value("${SOCKS_CATALOGUE_HOST}")
    private String socksCatalogueHost;
    @Value("${SHOES_CATALOGUE_HOST}")
    private String shoesCatalogueHost;

    @Override
    public void configure() throws Exception {
        
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
                .apiProperty("schemes", "http")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/catalogue")
            .description("List all products (socks & shoes) from the shop catalogue database")
            .get()
            .outType(ProductsList.class)
            .route().routeId("catalogue-api")
            .multicast(new ProductAggregationStrategy())
            .parallelProcessing()
            .to("direct:socksImplRemote", "direct:shoesImplRemote");

        rest("/catalogue/tags")
            .description("List all tags (socks & shoes) from the shop catalogue database")
            .get()
            .outType(TagList.class)
            .route().routeId("catalogue-tags-api")
            .multicast(new TagAggregationStrategy())
            .parallelProcessing()
            .to("direct:socksTagsImplRemote", "direct:shoesTagsImplRemote");            

        rest("/catalogue/{id}")
            .description("Size of catalogue database")
            .get()
            .outType(Product.class)
            .route().routeId("catalogue-query-api")
            .multicast(new CatalogueQueryAggregationStrategy())
            .parallelProcessing()
            .to("direct:querySocksService", "direct:queryShoesService")
            .end();            

        rest("/catalogue/size")
            .description("Size of catalogue database")
            .get()
            .outType(CatalogueSizeResponse.class)
            .route().routeId("catalogue-size-api")
            .multicast(new CatalogueSizeAggregationStrategy())
            .parallelProcessing()
            .to("direct:socksImplRemote", "direct:shoesImplRemote");            

        from("direct:socksImplRemote").description("Socks catalogue REST service implementation route")
            .log("Calling socks endpoint...")
            .streamCaching()
            .toD("http4://{{socksCatalogueHost}}/catalogue?bridgeEndpoint=true")
            .convertBodyTo(String.class)
            // .log("Sock raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, SocksList.class);
    
        from("direct:shoesImplRemote").description("Shoes catalogue REST service implementation route")
            .log("Calling shoes endpoint...")
            .streamCaching()
            .toD("http4://{{shoesCatalogueHost}}/shoes?bridgeEndpoint=true")
            .convertBodyTo(String.class)
            // .log("Shoe raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, ShoesList.class);

        from("direct:querySocksService").description("Queries Socks catalogue REST service implementation route")
            .log("Quering socks endpoint (at {{socksCatalogueHost}}) with id: ${header.id}")
            .streamCaching()
            .setHeader(Exchange.HTTP_PATH)
            .simple("catalogue/${header.id}")
            .toD("http4://{{socksCatalogueHost}}?bridgeEndpoint=true&throwExceptionOnFailure=false")
            .convertBodyTo(String.class)
            .choice()
                .when()
                    .simple("${header.CamelHttpResponseCode} == 200")
                    .to("direct:unmarshalSocksResponse")
                .otherwise()
                    .log("response from Socks backend service: ${header.CamelHttpResponseCode}")
                    .setBody(constant("not able to query shoes catalogue!"))
            .end();

        from("direct:unmarshalSocksResponse")
            .log("unmarshelling Socks response...")
            .unmarshal().json(JsonLibrary.Jackson, Sock.class);

        from("direct:queryShoesService").description("Queries Shoes catalogue REST service implementation route")
            .log("Quering shoes endpoint (at {{shoesCatalogueHost}}) with id: ${header.id}")
            .streamCaching()
            .setHeader(Exchange.HTTP_PATH)
            .simple("shoes/${header.id}")
            .toD("http4://{{shoesCatalogueHost}}?bridgeEndpoint=true&throwExceptionOnFailure=false")
            .convertBodyTo(String.class)
            .choice()
                .when()
                    .simple("${header.CamelHttpResponseCode} == 200")
                    .to("direct:unmarshalShoesResponse")
                .otherwise()
                    .log("response from shoes backend service: ${header.CamelHttpResponseCode}")
                    .setBody(constant("not able to query shoes catalogue!"))
            .end();
        
        from("direct:unmarshalShoesResponse")
            .log("unmarshelling Shoes response...")
            .unmarshal().json(JsonLibrary.Jackson, Shoe.class);

        from("direct:socksTagsImplRemote").description("Socks' tags catalogue REST service implementation route")
            .log("Calling socks' tags endpoint...")
            .streamCaching()
            .toD("http4://{{socksCatalogueHost}}/tags?bridgeEndpoint=true")
            .convertBodyTo(String.class)
            // .log("Sock raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, SockTagList.class);
    
        from("direct:shoesTagsImplRemote").description("Shoes' Tags catalogue REST service implementation route")
            .log("Calling shoes' tags endpoint...")
            .streamCaching()
            .toD("http4://{{shoesCatalogueHost}}/tags?bridgeEndpoint=true")
            .convertBodyTo(String.class)
            // .log("Shoe raw Body: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, ShoeTagList.class);
    }

}