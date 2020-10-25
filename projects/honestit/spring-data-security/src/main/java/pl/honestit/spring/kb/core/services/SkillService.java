package pl.honestit.spring.kb.core.services;

import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Service
public class SkillService {

    public List<TopSkillDTO> getTopSkills(int topSkillsCount) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getTopSkillDTOS();
    }

    public List<SkillDTO> getAllSkills() {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getSkillDTOS();
    }
}
