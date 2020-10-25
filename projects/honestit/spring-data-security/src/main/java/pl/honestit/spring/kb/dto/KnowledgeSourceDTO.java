package pl.honestit.spring.kb.dto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class KnowledgeSourceDTO {

    private Long id;
    private String name;
    private String description;
    private String url;
    private Set<SkillDTO> connectedSkills;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<SkillDTO> getConnectedSkills() {
        return connectedSkills;
    }

    public void setConnectedSkills(Set<SkillDTO> connectedSkills) {
        this.connectedSkills = connectedSkills;
    }

    public Set<String> getConnectedSkillsNames() {
        return connectedSkills.stream().map(SkillDTO::getName).collect(Collectors.toSet());
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeSourceDTO that = (KnowledgeSourceDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "KnowledgeSourceDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", connectedSkills=" + connectedSkills +
                ", active=" + active +
                '}';
    }
}
