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
 * A OpportunityQuestion.
 */

@Document(collection = "opportunity_question")
public class OpportunityQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 150)
    @Field("description")
    private String description;

    @Field("date")
    private LocalDate date;

    @Field("votes")
    private Integer votes;

    @Field("owner_login")
    private String ownerLogin;

    @Field("answer")
    private String answer;

    @Field("votedUsers")
    private List<String> votedUsers;

    public List<String> getVotedUsers() {
        return votedUsers;
    }

    public void setVotedUsers(List<String> votedUsers) {
        this.votedUsers = votedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public OpportunityQuestion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public OpportunityQuestion date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getVotes() {
        return votes;
    }

    public OpportunityQuestion votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public OpportunityQuestion ownerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
        return this;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getAnswer() {
        return answer;
    }

    public OpportunityQuestion answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpportunityQuestion opportunityQuestion = (OpportunityQuestion) o;
        if (opportunityQuestion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, opportunityQuestion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OpportunityQuestion{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", date='" + date + "'" +
            ", votes='" + votes + "'" +
            ", ownerLogin='" + ownerLogin + "'" +
            ", answer='" + answer + "'" +
            '}';
    }

    public void upVote(){
        votes ++;
    }

    public void downVote(){
        votes --;
    }
}
