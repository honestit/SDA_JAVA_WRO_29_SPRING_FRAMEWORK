package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Service
@Slf4j @RequiredArgsConstructor
public class SkillService {

    private final SkillService skillService;

    public List<TopSkillDTO> getTopSkills(int topSkillsCount) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getTopSkillDTOS();
    }

    public List<SkillDTO> getAllSkills() {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getSkillDTOS();
    }
}
