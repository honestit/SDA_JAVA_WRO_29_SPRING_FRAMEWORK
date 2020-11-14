package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repository.KnowledgeSourceRepository;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class KnowledgeSourceService {

    private final KnowledgeSourceRepository knowledgeSourceRepository;
    private final UserRepository userRepository;


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
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return true;
    }
}
