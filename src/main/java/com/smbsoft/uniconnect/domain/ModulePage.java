package com.smbsoft.uniconnect.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A ModulePage.
 */

@Document(collection = "module_page")
public class ModulePage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(min = 5, max = 40)
    @Field("title")
    private String title;

    @Size(min = 5, max = 150)
    @Field("description")
    private String description;

    @NotNull
    @Field("module_code")
    private String moduleCode;

    @Field("targets")
    private List<List<String>> targets;

    @Field("posts")
    private List<String> posts;

    public List<List<String>> getTargets() {
        return targets;
    }

    public void setTargets(List<List<String>> targets) {
        this.targets = targets;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
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

    public ModulePage title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ModulePage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public ModulePage moduleCode(String moduleCode) {
        this.moduleCode = moduleCode;
        return this;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModulePage modulePage = (ModulePage) o;
        if (modulePage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, modulePage.id);
    }

    public void addPost(String postId){
        if (posts==null)
            posts = new ArrayList<>();

        posts.add(postId);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ModulePage{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", moduleCode='" + moduleCode + "'" +
            '}';
    }
}
