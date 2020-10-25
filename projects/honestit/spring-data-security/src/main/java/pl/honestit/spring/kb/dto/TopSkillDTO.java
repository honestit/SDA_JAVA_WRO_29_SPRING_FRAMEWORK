package pl.honestit.spring.kb.dto;

import java.util.Objects;

public class TopSkillDTO {

    private String name;
    private String category;
    private String bestUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBestUser() {
        return bestUser;
    }

    public void setBestUser(String bestUser) {
        this.bestUser = bestUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopSkillDTO topSkill = (TopSkillDTO) o;
        return Objects.equals(name, topSkill.name) &&
                Objects.equals(category, topSkill.category) &&
                Objects.equals(bestUser, topSkill.bestUser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, category, bestUser);
    }

    @Override
    public String toString() {
        return "TopSkillDTO{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", bestUser='" + bestUser + '\'' +
                '}';
    }
}
