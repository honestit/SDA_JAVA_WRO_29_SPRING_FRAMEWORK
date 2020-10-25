package pl.honestit.spring.kb.dto;

import java.util.Objects;
import java.util.Set;

public class AddKnowledgeSourceDTO {

    private String name;
    private String description;
    private String url;
    private Set<Long> connectedSkillsIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Long> getConnectedSkillsIds() {
        return connectedSkillsIds;
    }

    public void setConnectedSkillsIds(Set<Long> connectedSkillsIds) {
        this.connectedSkillsIds = connectedSkillsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddKnowledgeSourceDTO that = (AddKnowledgeSourceDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "AddKnowledgeSourceDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", connectedSkillsIds=" + connectedSkillsIds +
                '}';
    }
}
