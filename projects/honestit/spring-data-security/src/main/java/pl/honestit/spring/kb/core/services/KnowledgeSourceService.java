package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.repository.KnowledgeSourceRepository;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Service
@Slf4j @RequiredArgsConstructor
public class KnowledgeSourceService {

    private final KnowledgeSourceRepository knowledgeSourceRepository;


    public List<KnowledgeSourceDTO> getSourcesKnownByUser(LoggedUserDTO user) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getKnowledgeSourceDTOS(1, 6);
    }

    public List<KnowledgeSourceDTO> getSourcesUnknownByUser(LoggedUserDTO user) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getKnowledgeSourceDTOS(6, 11);
    }

    public KnowledgeSourceDTO getSource(Long sourceId) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getKnowledgeSourceDTOS(sourceId, sourceId + 1).iterator().next();
    }

    public List<KnowledgeSourceDTO> getAllSources() {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getKnowledgeSourceDTOS(1, 15);
    }

    public boolean deleteSource(Long sourceId) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return true;
    }

    public boolean activateSource(Long sourceId) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return true;
    }

    public boolean addNewSource(AddKnowledgeSourceDTO newKnowledgeSource) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return true;
    }
}
