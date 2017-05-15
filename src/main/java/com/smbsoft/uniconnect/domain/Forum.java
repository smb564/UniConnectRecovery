package com.smbsoft.uniconnect.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Forum.
 */

@Document(collection = "forum")
public class Forum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("posts")
    List<String> posts;

    @Field("title")
    String title;

    @Field("description")
    String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosts(List<String> posts){
        this.posts = posts;
    }

    public List<String> getPosts() {
        return posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Forum forum = (Forum) o;
        if (forum.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, forum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Forum{" +
            "id=" + id +
            '}';
    }
}
