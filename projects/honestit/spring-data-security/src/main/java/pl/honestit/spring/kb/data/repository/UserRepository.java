package pl.honestit.spring.kb.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.honestit.spring.kb.data.model.Skill;
import pl.honestit.spring.kb.data.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query(value = "SELECT s FROM User u JOIN u.knownSources uks JOIN uks.connectedSkills s WHERE u.id = ?1")
    List<Skill> findAllNonDistinctObtainedSkillsForUser(Long id);

    @Query("SELECT u FROM User u LEFT JOIN u.knownSources uks LEFT JOIN uks.connectedSkills s GROUP BY u ORDER BY COUNT(s) DESC")
    List<User> findTopUsers(Pageable pageable);
}
