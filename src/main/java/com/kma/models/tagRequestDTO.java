package com.kma.models;

import com.kma.enums.TagCategory;

public class tagRequestDTO {
    private String tagName;
    private String description;
    private TagCategory category;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TagCategory getCategory() {
        return category;
    }

    public void setCategory(TagCategory category) {
        this.category = category;
    }
}
