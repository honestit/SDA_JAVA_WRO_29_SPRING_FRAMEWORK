package pl.honestit.spring.kb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.honestit.spring.kb.data.model.Skill;
import pl.honestit.spring.kb.data.model.User;
import pl.honestit.spring.kb.data.repository.SkillRepository;
import pl.honestit.spring.kb.data.repository.UserRepository;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.dto.TopSkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional
@Slf4j @RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public List<TopSkillDTO> getTopSkills(int topSkillsCount) {
        // Pobieramy wszystkich użytkowników, aby uzyskać ich umiejętności.
        // W końcu interesują nas tylko umiejętności posiadane przez użytkowników
        List<User> allUsers = userRepository.findAll();

        // Tworzymy mapę użytkowników i ich umiejętności
        Map<User, List<Skill>> usersSkills = new HashMap<>();
        allUsers.forEach(user -> usersSkills.put(user,
                userRepository.findAllNonDistinctObtainedSkillsForUser(user.getId())));
        // Teraz musimy stworzyć mapę obiektów TopSkillDTO, dla której wartościami będzie lista
        // użytkowników którzy tą umiejętność posiadają. Później rozmiar tej listy posłuży nam
        // do wyboru najbardziej popularnych umiejętności, a ilość wystąpień konkretnego użytkownika
        // na tej liście do określenia kto jest TOP w tej umiejętności.
        Map<TopSkillDTO, List<User>> topSkillsToUsers = new HashMap<>();
        usersSkills.forEach((user, skills) -> {
            skills.forEach(skill -> {
                TopSkillDTO topSkillDTO = new TopSkillDTO();
                topSkillDTO.setName(skill.getName());
                topSkillDTO.setCategory(skill.getCategory());
                topSkillsToUsers.merge(topSkillDTO, new ArrayList<>(Collections.singletonList(user)),(oldUsers, newUsers) -> {
                    oldUsers.addAll(newUsers);
                    return oldUsers;
                });
            });
        });

        // Teraz zapiszemy wpisy w mapie w postaci uporządkowanej listy i pobierzemy z niej sublistę o oczekiwanej ilości elementów
        List<Map.Entry<TopSkillDTO, List<User>>> topSkillsEntries = topSkillsToUsers
                .entrySet()
                .stream()
                .sorted((o1, o2) -> - (o1.getValue().size() - o2.getValue().size()))
                .collect(Collectors.toList());
        List<Map.Entry<TopSkillDTO, List<User>>> subList = topSkillsEntries
                .subList(0, Math.min(topSkillsCount, topSkillsEntries.size()));

        // Pozostał teraz dla każdego obiektu TopSkillDTO znaleźć użytkownika, który występuje na liście najwięcej razy.
        List<TopSkillDTO> topSkillsList = new ArrayList<>();
        subList.forEach(topSkillDTOListEntry -> {
            TopSkillDTO topSkill = topSkillDTOListEntry.getKey();
            Map<User, Integer> countingMap = new HashMap<>();
            topSkillDTOListEntry.getValue().forEach(user -> {
                countingMap.merge(user, 1, Integer::sum);
            });
            Set<Map.Entry<User, Integer>> countingMapEntries = countingMap.entrySet();
            int topCount = 0;
            User topUser = null;
            for (Map.Entry<User, Integer> entry : countingMapEntries) {
                if (topUser == null || topCount < entry.getValue()) {
                    topUser = entry.getKey();
                    topCount = entry.getValue();
                }
            }
            topSkill.setBestUser(topUser.getLogin());
            topSkillsList.add(topSkill);
        });

        return topSkillsList;
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
