package io.github.microservices.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag
 */
public class TagList {

    private List<String> tags = new ArrayList<>();

    public TagList(){
        
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
    
    public void addTag(String aTag) {
        this.tags.add(aTag);
    }

    public void removeTag(String aTag) {
        this.tags.remove(aTag);
    }

}