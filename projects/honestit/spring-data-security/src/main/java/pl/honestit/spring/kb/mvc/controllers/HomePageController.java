package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.honestit.spring.kb.core.services.SkillService;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.dto.TopUserDTO;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomePageController {

    private UserService userService;
    private SkillService skillService;

    @Autowired
    public HomePageController(UserService userService, SkillService skillService) {
        this.userService = userService;
        this.skillService = skillService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        List<TopUserDTO> topUsers = userService.getTopUsers(10);
        model.addAttribute("topUsers", topUsers);

        List<TopSkillDTO> topSkills = skillService.getTopSkills(10);
        model.addAttribute("topSkills", topSkills);
        return "home";
    }

    @PostMapping
    public String postHomePage() {
        return "redirect:/";
    }
}
