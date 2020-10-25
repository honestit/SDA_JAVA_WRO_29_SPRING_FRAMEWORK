package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.honestit.spring.kb.core.services.KnowledgeSourceService;
import pl.honestit.spring.kb.core.services.SkillService;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Controller
@RequestMapping("/user/sources")
public class UserSourcesController {

    private UserService userService;
    private SkillService skillService;
    private KnowledgeSourceService knowledgeSourceService;

    @Autowired
    public UserSourcesController(UserService userService, SkillService skillService, KnowledgeSourceService knowledgeSourceService) {
        this.userService = userService;
        this.skillService = skillService;
        this.knowledgeSourceService = knowledgeSourceService;
    }

    @GetMapping
    public String getAvailableSourcesPage(Model model, @SessionAttribute(required = false) LoggedUserDTO user) {
        if (user == null) {
            user = TestDataGenerator.getLoggedUserDTO("user");
        }
        List<KnowledgeSourceDTO> sources = knowledgeSourceService.getSourcesKnownByUser(user);
        List<KnowledgeSourceDTO> availableSources = knowledgeSourceService.getSourcesUnknownByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("sources", sources);
        model.addAttribute("availableSources", availableSources);
        return "sources";
    }

    @PostMapping("/confirm")
    public String confirmSource(Long sourceId, @SessionAttribute(required = false) LoggedUserDTO user) {
        if (user == null) {
            user = TestDataGenerator.getLoggedUserDTO("user");
        }
        KnowledgeSourceDTO source = knowledgeSourceService.getSource(sourceId);
        userService.addNewSource(user, source);
        return "redirect:/user/sources";
    }
}
