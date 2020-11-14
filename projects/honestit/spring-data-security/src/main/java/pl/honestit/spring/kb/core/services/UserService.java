package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.model.KnowledgeSource;
import pl.honestit.spring.kb.data.model.Skill;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repository.KnowledgeSourceRepository;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KnowledgeSourceRepository knowledgeSourceRepository;

    public List<TopUserDTO> getTopUsers(int topUsersCount) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getTopUserDTOS();
    }

    public List<SkillDTO> getSkillsForUser(LoggedUserDTO user) {
        return userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())
                .stream()
                .filter(Objects::nonNull)
                .map(skill -> {
                    SkillDTO skillDTO = new SkillDTO();
                    skillDTO.setId(skill.getId());
                    skillDTO.setName(skill.getName());
                    skillDTO.setCategory(skill.getCategory());
                    return skillDTO;
                })
                .collect(Collectors.toList());
    }

    public void addNewSource(LoggedUserDTO user, KnowledgeSourceDTO source) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(source.getId()).orElseThrow(IllegalArgumentException::new);
        // Sprawdzamy czy źródło jest nieaktywne, bo jeżeli tak to nie można go potwierdzić
        if (!knowledgeSource.getActive()) {
            throw new IllegalArgumentException("Nie można potwierdzić nieaktywnego źródła wiedzy");
        }
        // Dodajemy źródłowie wiedzy użytkownikowi tylko wtedy gdy go jeszcze nie ma
        if (!userEntity.getKnownSources().contains(knowledgeSource)) {
            userEntity.getKnownSources().add(knowledgeSource);
        }
    }

    public boolean checkCredentials(String login, String password) {
        return userRepository.findByLogin(login)
                .map(User::getPassword)
                .map(pass -> pass.equals(password))
                .orElse(false);
    }

    public LoggedUserDTO getUser(String login, String password) {
        return userRepository.findByLogin(login)
                .map(user -> LoggedUserDTO.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build()).orElseThrow(() -> new IllegalArgumentException("Niepoprawny login"));
    }

    public LoggedUserDTO getUser(String login) {
        return userRepository.findByLogin(login)
                .map(user -> LoggedUserDTO.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build()).orElseThrow(() -> new IllegalArgumentException("Niepoprawny login"));
    }
}
