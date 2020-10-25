package pl.honestit.spring.web.app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.honestit.spring.web.app.dto.UserDTO;
import pl.honestit.spring.web.app.model.User;
import pl.honestit.spring.web.app.session.UserIdGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserIdGenerator userIdGenerator;

    private ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    public Long saveUser(UserDTO userDTO) {
        User user = User.builder()
                .id(userIdGenerator.getNextId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .country(userDTO.getCountry())
                .versionId(userDTO.getVersionId())
                .creationDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .build();

        users.put(user.getId(), user);
        return user.getId();
    }

    public UserDTO getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .gender(user.getGender())
                .country(user.getCountry())
                .versionId(user.getVersionId())
                .build();
    }
}
