package com.redhat.fuse.boosters.rest.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
  
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer count;
  @JsonProperty(value = "imageUrl")
  private List<String> imageUrlList = new ArrayList<>();
  @JsonProperty(value = "tag")
  private List<String> tagList = new ArrayList<>();
  private String productType;
  
  public Product(){
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the price
   */
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /**
   * @return the count
   */
  public Integer getCount() {
    return count;
  }

  /**
   * @param count the count to set
   */
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   * remove a Url to the imageUrls list
   */
  public void removeImageUrl(String imageUrl) {
    this.imageUrlList.remove(imageUrl);
  }

  /**
   * add a new Url to the imageUrls list
   */
  public void addImageUrl(String imageUrl) {
    this.imageUrlList.add(imageUrl);
  }

  /**
   * remove a Tag to the tags list
   */
  public void removeTag(String tag) {
    this.tagList.remove(tag);
  }

  /**
   * add a new Tag to the tags list
   */
  public void addTag(String tag) {
    this.tagList.add(tag);
  }

  @Override
  public String toString() {
    return "Product [count=" + count + ", description=" + description + ", id=" + id + ", imageUrl=" + imageUrlList
        + ", name=" + name + ", price=" + price + ", tag=" + tagList + "]";
  }

  /**
   * @return the productType
   */
  public String getProductType() {
    return productType;
  }

  /**
   * @param productType the productType to set
   */
  public void setProductType(String productType) {
    this.productType = productType;
  }

}
