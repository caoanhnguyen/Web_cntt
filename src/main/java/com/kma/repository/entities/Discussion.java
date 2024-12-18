package com.kma.repository.entities;

import com.kma.enums.DiscussionStatus;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name="discussion")
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discussionId;

    @Column(name="title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name="content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="createAt")
    private Date createAt = new Date(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscussionStatus status; // UP or DOWN

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")    // Chứa mã SV hoặc mã NV
    private User user;

    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL)
    private Set<Answer> answers;

    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL)
    private Set<Vote> votes;

    @ManyToMany
    @JoinTable(
            name = "discussion_tag",
            joinColumns = @JoinColumn(name = "discussionId"),
            inverseJoinColumns = @JoinColumn(name = "tagId")
    )
    private Set<Tag> tags;

    public DiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(DiscussionStatus status) {
        this.status = status;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Integer discussionId) {
        this.discussionId = discussionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
