package io.github.microservices.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag
 */
public class SockTagList {

    private List<String> tags = new ArrayList<>();

    public SockTagList(){
        
    }

    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
}