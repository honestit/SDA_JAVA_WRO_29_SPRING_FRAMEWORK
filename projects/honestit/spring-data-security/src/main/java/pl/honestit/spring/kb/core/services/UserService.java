package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<TopUserDTO> getTopUsers(int topUsersCount) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getTopUserDTOS();
    }

    public List<SkillDTO> getSkillsForUser(LoggedUserDTO user) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getSkillDTOS();
    }

    public void addNewSource(LoggedUserDTO user, KnowledgeSourceDTO source) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data
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
