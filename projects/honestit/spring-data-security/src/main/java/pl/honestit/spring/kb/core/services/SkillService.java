package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.repository.SkillRepository;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<TopSkillDTO> getTopSkills(int topSkillsCount) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getTopSkillDTOS();
    }

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream().map(skill -> {
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setId(skill.getId());
            skillDTO.setName(skill.getName());
            skillDTO.setCategory(skill.getCategory());
            return skillDTO;
        }).collect(Collectors.toList());
    }
}
