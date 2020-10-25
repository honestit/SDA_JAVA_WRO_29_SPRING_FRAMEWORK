package pl.honestit.spring.kb.utils;

import pl.honestit.spring.kb.dto.*;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TestDataGenerator {

    public static List<TopUserDTO> getTopUserDTOS() {
        return IntStream.range(1, 11).mapToObj(value -> {
            TopUserDTO topUser = new TopUserDTO();
            topUser.setLogin("Login_" + value);
            topUser.setUniqueSkillsCount(value);
            topUser.setAllSkillsCount(value * value);
            topUser.setKnowledgeSourceCount((int) (value / 1.5));
            return topUser;
        }).sorted(Comparator.comparing(TopUserDTO::getAllSkillsCount).reversed())
                .collect(Collectors.toList());
    }

    public static List<SkillDTO> getSkillDTOS() {
        return LongStream.range(1, 11).mapToObj(value -> {
            SkillDTO skill = new SkillDTO();
            skill.setId(value);
            skill.setName("Name_" + value);
            skill.setCategory("Category_" + value);
            return skill;
        }).collect(Collectors.toList());
    }

    public static LoggedUserDTO getLoggedUserDTO(String login) {
        LoggedUserDTO loggedUser = new LoggedUserDTO();
        loggedUser.setId(1L);
        loggedUser.setFirstName("FirstName");
        loggedUser.setLastName("LastName");
        loggedUser.setLogin(login);
        return loggedUser;
    }

    public static List<TopSkillDTO> getTopSkillDTOS() {
        return IntStream.range(1, 11).mapToObj(value -> {
            TopSkillDTO topSkill = new TopSkillDTO();
            topSkill.setName("Name_" + value);
            topSkill.setCategory("Category_" + value);
            topSkill.setBestUser("Login_" + (1 + new Random().nextInt(10)));
            return topSkill;
        }).collect(Collectors.toList());
    }

    public static List<KnowledgeSourceDTO> getKnowledgeSourceDTOS(long i, long i2) {
        return LongStream.range(i, i2).mapToObj(value -> {
            KnowledgeSourceDTO knowledgeSource = new KnowledgeSourceDTO();
            knowledgeSource.setId(value);
            knowledgeSource.setName("Name_" + value);
            knowledgeSource.setDescription("Description_" + value);
            knowledgeSource.setActive(knowledgeSource.getId() <= 11);
            knowledgeSource.setUrl(null);
            knowledgeSource.setConnectedSkills(
                    LongStream.range(1, 11).mapToObj(value2 -> {
                        if (Math.random() > 0.5) {
                            SkillDTO skill = new SkillDTO();
                            skill.setId(value2);
                            skill.setName("Name_" + value2);
                            skill.setCategory("Category_" + value2);
                            return skill;
                        } else {
                            return new SkillDTO();
                        }
                    }).filter(skill -> skill.getId() != null).collect(Collectors.toSet())
            );
            return knowledgeSource;
        }).collect(Collectors.toList());
    }
}
