package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopUserDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

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
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return true;
    }

    public LoggedUserDTO getUser(String login, String password) {
        // TODO Uzupełnij implementację z wykorzystaniem Spring Data

        return TestDataGenerator.getLoggedUserDTO(login);
    }

    public LoggedUserDTO getUser(String login) {
        return TestDataGenerator.getLoggedUserDTO(login);
    }
}
