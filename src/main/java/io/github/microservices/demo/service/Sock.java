package io.github.microservices.demo.service;

import java.math.BigDecimal;

public class Sock extends Product {
  
  public Sock(){}

  public Sock(String id, String name, String description, BigDecimal price, Integer count) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setPrice(price);
    this.setCount(count);
  }

  @Override public String getProductType() {
    return "sock";
  }
}
