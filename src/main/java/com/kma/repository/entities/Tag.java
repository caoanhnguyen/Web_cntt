package com.kma.repository.entities;

import com.kma.enums.TagCategory;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name="description")
    private String description;

    @Enumerated(EnumType.STRING) // Sử dụng Enum để định nghĩa loại môn học
    @Column(name="category", nullable = false)
    private TagCategory category;

    @ManyToMany(mappedBy = "tags")
    private Set<Discussion> discussions;

    public TagCategory getCategory() {
        return category;
    }

    public void setCategory(TagCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(Set<Discussion> discussions) {
        this.discussions = discussions;
    }
}
