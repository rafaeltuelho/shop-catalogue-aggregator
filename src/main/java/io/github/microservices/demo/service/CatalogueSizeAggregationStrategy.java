package io.github.microservices.demo.service;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class CatalogueSizeAggregationStrategy implements AggregationStrategy {

  @Override
  public Exchange aggregate(Exchange ex1, Exchange ex2) {
    if (ex1 == null) {
      return ex2;
    } else {
      List<Sock> socks = ex1.getIn().getBody(SocksList.class);
      System.out.println("socks list size: " + socks.size());
      List<Shoe> shoes = ex2.getIn().getBody(ShoesList.class);
      System.out.println("shoes list size: " + shoes.size());

      ex1.getIn().setBody(
        new CatalogueSizeResponse(socks.size() + shoes.size()));
        
      return ex1;
    }
  }
}
