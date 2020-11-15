package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.honestit.spring.kb.core.services.KnowledgeSourceService;
import pl.honestit.spring.kb.core.services.SkillService;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministrationController {

    private UserService userService;
    private SkillService skillService;
    private KnowledgeSourceService knowledgeSourceService;

    @Autowired
    public AdministrationController(UserService userService, SkillService skillService, KnowledgeSourceService knowledgeSourceService) {
        this.userService = userService;
        this.skillService = skillService;
        this.knowledgeSourceService = knowledgeSourceService;
    }

    @ModelAttribute("sources")
    public List<KnowledgeSourceDTO> sources() {
        return knowledgeSourceService.getAllSources();
    }

    @ModelAttribute("availableSkills")
    public List<SkillDTO> availableSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping
    public String prepareAdminPanel(Model model) {
        AddKnowledgeSourceDTO newSource = new AddKnowledgeSourceDTO();
        model.addAttribute("newSource", newSource);
        return "admin";
    }

    @PostMapping("/sources/add")
    // private String addNewKnowledgeSource(@RequestParam String name, String description, String url, Set<Long> connectedSkillsIds)
    // GET /source/add?name=abc&description=z&url=abab&connectedSkillsIds=1&connectedSkillsIds=4dvbg7&connectedSkills=14
    // GET /source/add?description=z&url=abab&connectedSkillsIds=1&connectedSkillsIds=4dvbg7&connectedSkills=14
    // HttpServletRequest request = ...
    // String[] values = request.getParameterValues("connectedSkillsIds");
    // Set<Long> ids = Stream.of(values).map(Long::parseLong).collect(Collectors.toSet());

    /* Parametr BindingResult musi być bezpośrednio następnym po parametrze walidowanym */
    private String addNewKnowledgeSource(
            @Valid @ModelAttribute("newSource") AddKnowledgeSourceDTO newKnowledgeSource,
            BindingResult bindings ) {

        if (bindings.hasErrors()) {
            return "admin";
        }
        knowledgeSourceService.addNewSource(newKnowledgeSource);
        return "redirect:/admin";
    }

    @PostMapping("/sources/delete")
    private String deleteSource(Long sourceId) {
        knowledgeSourceService.deleteSource(sourceId);
        return "redirect:/admin";
    }

    @PostMapping("/sources/activate")
    public String activateSource(Long sourceId) {
        knowledgeSourceService.activateSource(sourceId);
        return "redirect:/admin";
    }
}