package com.smbsoft.uniconnect.domain;

import java.io.Serializable;
import java.util.List;

public class OpportunityFilter implements Serializable {
    private List<String> tags;

    private List<List<String>> targets;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<List<String>> getTargets() {
        return targets;
    }

    public void setTargets(List<List<String>> targets) {
        this.targets = targets;
    }
}
