package pl.honestit.spring.kb.dto;

import java.util.Objects;

public class TopUserDTO {

    private String login;
    private Integer uniqueSkillsCount;
    private Integer allSkillsCount;
    private Integer knowledgeSourceCount;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getUniqueSkillsCount() {
        return uniqueSkillsCount;
    }

    public void setUniqueSkillsCount(Integer uniqueSkillsCount) {
        this.uniqueSkillsCount = uniqueSkillsCount;
    }

    public Integer getAllSkillsCount() {
        return allSkillsCount;
    }

    public void setAllSkillsCount(Integer allSkillsCount) {
        this.allSkillsCount = allSkillsCount;
    }

    public Integer getKnowledgeSourceCount() {
        return knowledgeSourceCount;
    }

    public void setKnowledgeSourceCount(Integer knowledgeSourceCount) {
        this.knowledgeSourceCount = knowledgeSourceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopUserDTO topUser = (TopUserDTO) o;
        return Objects.equals(login, topUser.login) &&
                Objects.equals(uniqueSkillsCount, topUser.uniqueSkillsCount) &&
                Objects.equals(allSkillsCount, topUser.allSkillsCount) &&
                Objects.equals(knowledgeSourceCount, topUser.knowledgeSourceCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(login, uniqueSkillsCount, allSkillsCount, knowledgeSourceCount);
    }

    @Override
    public String toString() {
        return "TopUserDTO{" +
                "login='" + login + '\'' +
                ", uniqueSkillsCount=" + uniqueSkillsCount +
                ", allSkillsCount=" + allSkillsCount +
                ", knowledgeSourceCount=" + knowledgeSourceCount +
                '}';
    }
}
