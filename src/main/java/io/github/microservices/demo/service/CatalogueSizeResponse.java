package io.github.microservices.demo.service;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CatalogueCountResponse
 */
public class CatalogueSizeResponse {

    @JsonProperty("err")
    private String error;
    private Integer size;

    public CatalogueSizeResponse(Integer size){
        this.size = size;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the count
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param count the count to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }
}