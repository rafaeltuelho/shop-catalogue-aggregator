package io.github.microservices.demo.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogueErrorResponse {
    @JsonProperty("error")
    private String errorMsg;
    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("status_check")
    private String statusMsg;

    public CatalogueErrorResponse() {
    }

    public CatalogueErrorResponse(String errorMsg, Integer statusCode, String statusMsg) {
        this.errorMsg = errorMsg;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusMsg
     */
    public String getStatusMsg() {
        return statusMsg;
    }

    /**
     * @param statusMsg the statusMsg to set
     */
    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}