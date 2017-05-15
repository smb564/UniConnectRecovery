package com.smbsoft.uniconnect.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A Post.
 */

@Document(collection = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 5, max = 40)
    @Field("title")
    private String title;

    @NotNull
    @Size(min = 5, max = 150)
    @Field("description")
    private String description;

    @Field("date")
    private LocalDate date;

    @Field("votes")
    private Integer votes;

    @Field("owner_login")
    private String ownerLogin;

    @Field("vote_users")
    private List<String> voteUsers;

    @Field("tags")
    private List<String> tags;

    @Field("comments")
    private List<String> comments;

    public List<String> getVoteUsers() {
        return voteUsers;
    }

    public void setVoteUsers(List<String> voteUsers) {
        this.voteUsers = voteUsers;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Post title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Post description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public Post date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getVotes() {
        return votes;
    }

    public Post votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public Post ownerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
        return this;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        if (post.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", date='" + date + "'" +
            ", votes='" + votes + "'" +
            ", ownerLogin='" + ownerLogin + "'" +
            '}';
    }

    public void incrementVotes(){
        this.votes ++;
    }

    public void decrementVotes(){
        this.votes --;
    }
}
