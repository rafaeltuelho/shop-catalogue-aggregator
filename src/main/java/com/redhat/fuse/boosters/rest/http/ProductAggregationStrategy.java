package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class ProductAggregationStrategy implements AggregationStrategy {

  @Override
  public Exchange aggregate(Exchange ex1, Exchange ex2) {
    if (ex1 == null) {
      return ex2;
    } else {
      List<Sock> socks = ex1.getIn().getBody(SocksList.class);
      System.out.println("socks list size: " + socks.size());
      List<Shoe> shoes = ex2.getIn().getBody(ShoesList.class);
      System.out.println("shoes list size: " + shoes.size());

      List<Product> products = new ArrayList<>();

      for (Sock sock : socks) {
        // System.out.println("sock name: " + sock.getName());
        products.add(sock);
      }

      for (Shoe shoe : shoes) {
        // System.out.println("shoe name: " + shoe.getName());
        Product product = new Product();
        product.setId(shoe.getId());
        product.setName(shoe.getName());
        product.setDescription(shoe.getDescription());
        product.setCount(shoe.getCount());
        product.setPrice(shoe.getPrice());
        product.setProductType(shoe.getProductType());
        product.addImageUrl(shoe.getImageUrl1());
        product.addImageUrl(shoe.getImageUrl2());
        for (Tag tag : shoe.getTags()) {
          product.addTag(tag.getName());
        }
        products.add(product);
      }


      ex1.getIn().setBody(products);
      System.out.println("Products size: " + products.size());
      return ex1;
    }
  }
}
