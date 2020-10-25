package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.honestit.spring.kb.core.services.SkillService;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.*;

@Controller
@RequestMapping("/user/skills")
public class UserSkillsController {

    private UserService userService;
    private SkillService skillService;

    @Autowired
    public UserSkillsController(UserService userService, SkillService skillService) {
        this.userService = userService;
        this.skillService = skillService;
    }

    @GetMapping
    public String getUserSkillsPage(Model model, @SessionAttribute(required = false) LoggedUserDTO user) {
        if (user == null) {
            user = TestDataGenerator.getLoggedUserDTO("user");
        }
        List<SkillDTO> userSkills = userService.getSkillsForUser(user);

        Set<SkillDTO> uniqueSkills = new TreeSet<>(
                Comparator.comparing(SkillDTO::getCategory)
                .thenComparing(SkillDTO::getName));
        uniqueSkills.addAll(userSkills);

        Map<SkillDTO, Integer> skillsOccurrences = new HashMap<>(uniqueSkills.size());
        for (SkillDTO skill : userSkills) {
            skillsOccurrences.merge(skill, 1, (oldVal, newVal) -> oldVal + newVal);
        }

        model.addAttribute("user", user);
        model.addAttribute("uniqueSkills", uniqueSkills);
        model.addAttribute("skillsOccurrences", skillsOccurrences);
        return "skills";
    }
}
