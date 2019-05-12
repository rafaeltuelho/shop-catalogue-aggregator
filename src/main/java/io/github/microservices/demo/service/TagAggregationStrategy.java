package io.github.microservices.demo.service;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class TagAggregationStrategy implements AggregationStrategy {

  @Override
  public Exchange aggregate(Exchange ex1, Exchange ex2) {
    if (ex1 == null) {
      return ex2;
    } else {
      SockTagList sockTagList = ex1.getIn().getBody(SockTagList.class);
      System.out.println("socks' tag list size: " + sockTagList.getTags().size());
      ShoeTagList shoeTagList = ex2.getIn().getBody(ShoeTagList.class);
      System.out.println("shoes' tag list size: " + shoeTagList.size());

      TagList tags = new TagList();
      // add tags coming  from Socks catalogue service (which has the same data structure)
      tags.getTags().addAll(sockTagList.getTags());
   
      // add tags coming from Shoes catalogue service
      for (Tag shoeTag : shoeTagList) {
        tags.addTag(shoeTag.getName());
      }

      ex1.getIn().setBody(tags);
      System.out.println("Tags size: " + tags.getTags().size());
      return ex1;
    }
  }
}
