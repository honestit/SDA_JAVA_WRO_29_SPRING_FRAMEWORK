package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repository.KnowledgeSourceRepository;
import pl.honestit.spring.kb.data.repository.SkillRepository;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class KnowledgeSourceService {

    private final KnowledgeSourceRepository knowledgeSourceRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;


    public List<KnowledgeSourceDTO> getSourcesKnownByUser(LoggedUserDTO user) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        return userEntity.getKnownSources().stream().map(sourceEntity -> {
            KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
            sourceDTO.setId(sourceEntity.getId());
            sourceDTO.setName(sourceEntity.getName());
            sourceDTO.setDescription(sourceEntity.getDescription());
            sourceDTO.setUrl(sourceEntity.getUrl());
            sourceDTO.setActive(sourceEntity.getActive());
            sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());
                skillDTO.setCategory(skillEntity.getName());
                return skillDTO;
            }).collect(Collectors.toSet()));
            return sourceDTO;
        }).collect(Collectors.toList());
    }

    public List<KnowledgeSourceDTO> getSourcesUnknownByUser(LoggedUserDTO user) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        return knowledgeSourceRepository.findDistinctSourcesByActiveIsTrueAndKnowingUsersNotContains(userEntity)
                .stream().map(sourceEntity -> {
                    KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
                    sourceDTO.setId(sourceEntity.getId());
                    sourceDTO.setName(sourceEntity.getName());
                    sourceDTO.setDescription(sourceEntity.getDescription());
                    sourceDTO.setUrl(sourceEntity.getUrl());
                    sourceDTO.setActive(sourceEntity.getActive());
                    sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                        SkillDTO skillDTO = new SkillDTO();
                        skillDTO.setId(skillEntity.getId());
                        skillDTO.setName(skillEntity.getName());
                        skillDTO.setCategory(skillEntity.getName());
                        return skillDTO;
                    }).collect(Collectors.toSet()));
                    return sourceDTO;
                }).collect(Collectors.toList());
    }

    public KnowledgeSourceDTO getSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        KnowledgeSourceDTO knowledgeSourceDTO = new KnowledgeSourceDTO();
        knowledgeSourceDTO.setId(knowledgeSource.getId());
        knowledgeSourceDTO.setName(knowledgeSource.getName());
        knowledgeSourceDTO.setDescription(knowledgeSource.getDescription());
        knowledgeSourceDTO.setActive(knowledgeSource.getActive());
        knowledgeSourceDTO.setUrl(knowledgeSource.getUrl());
        knowledgeSourceDTO.setConnectedSkills(knowledgeSource.getConnectedSkills().stream().map(skillEntity -> {
            SkillDTO skillDTO = new SkillDTO();
            skillDTO.setId(skillEntity.getId());
            skillDTO.setName(skillEntity.getName());
            skillDTO.setCategory(skillEntity.getName());
            return skillDTO;
        }).collect(Collectors.toSet()));
        return knowledgeSourceDTO;
    }

    public List<KnowledgeSourceDTO> getAllSources() {
        return knowledgeSourceRepository.findAll().stream().map(sourceEntity -> {
            KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
            sourceDTO.setId(sourceEntity.getId());
            sourceDTO.setName(sourceEntity.getName());
            sourceDTO.setDescription(sourceEntity.getDescription());
            sourceDTO.setUrl(sourceEntity.getUrl());
            sourceDTO.setActive(sourceEntity.getActive());
            sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());
                skillDTO.setCategory(skillEntity.getName());
                return skillDTO;
            }).collect(Collectors.toSet()));
            return sourceDTO;
        }).collect(Collectors.toList());
    }

    public boolean deleteSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        if (knowledgeSource.getActive()) {
            return false;
        }
        knowledgeSourceRepository.delete(knowledgeSource);
        return true;
    }

    public boolean activateSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        knowledgeSource.setActive(true);
        return knowledgeSource.getActive();
    }

    public boolean addNewSource(AddKnowledgeSourceDTO newKnowledgeSource) {
        Objects.requireNonNull(newKnowledgeSource);
        Objects.requireNonNull(newKnowledgeSource.getName());
        Objects.requireNonNull(newKnowledgeSource.getConnectedSkillsIds());

        // Sprawdzamy czy źródło o takiej nazwie przypadkiem już nie istnieje
        String sourceName = newKnowledgeSource.getName();
        KnowledgeSource knowledgeSourceByName = knowledgeSourceRepository.findByName(sourceName).orElse(null);
        if (knowledgeSourceByName != null) {
            return false;
        }

        // Tworzymy podstawowy obiekt encji i uzupełniamy proste wartości
        KnowledgeSource entity = new KnowledgeSource();
        entity.setName(sourceName);
        entity.setDescription(newKnowledgeSource.getDescription());
        entity.setUrl(newKnowledgeSource.getUrl());

        // Pobieramy teraz listę umiejętności na podstawie listy identyfikatorów i ją ustawiamy gdy nie
        // jest pusta
        List<Skill> skills = skillRepository.findAllById(newKnowledgeSource.getConnectedSkillsIds());
        // Upewniamy się, że obie listy są sobie równe co do rozmiaru
        if (skills.size() != newKnowledgeSource.getConnectedSkillsIds().size()) {
            throw new IllegalStateException("Wskazana lista umiejętności i uzyskana lista umiejętności są różnej długości!");
        }
        // a jeżeli są puste, to kończymy działanie metody i nie zapisujemy encji
        if (skills.isEmpty()) {
            return false;
        }
        entity.getConnectedSkills().addAll(skills);

        // Na koniec zapisujemy encję w bazie danych
        knowledgeSourceRepository.save(entity);
        return true;
    }
}
