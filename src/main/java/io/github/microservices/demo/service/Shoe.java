package io.github.microservices.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Shoe extends Product {
  
  private String imageUrl1;
  private String imageUrl2;
  private List<Tag> tags;

  public Shoe() {}

  public Shoe(String id, String name, String description, BigDecimal price, Integer count, List<String> imageUrl, List<String> tag) {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setPrice(price);
    this.setCount(count);
  }

  public String getImageUrl1() {
    return this.imageUrl1;
  }

  public void setImageUrl1(String imageUrl1) {
    this.imageUrl1 = imageUrl1;
  }

  public String getImageUrl2() {
    return this.imageUrl2;
  }

  public void setImageUrl2(String imageUrl2) {
    this.imageUrl2 = imageUrl2;
  }

  public List<Tag> getTags() {
    return this.tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
  
  @Override public String getProductType() {
    return "shoe";
  }
}
